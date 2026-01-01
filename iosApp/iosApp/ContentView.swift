import SwiftUI
import shared

struct ContentView: View {
    var body: some View {
        TabView {
            PokemonListView()
                .tabItem {
                    Image(systemName: "list.bullet")
                    Text("図鑑")
                }

            MenuView()
                .tabItem {
                    Image(systemName: "line.3.horizontal")
                    Text("メニュー")
                }
        }
    }
}

enum PokemonListViewUiState {
    case loading
    case success([PokemonListItem])
    case error(String)
}

struct PokemonListView: View {
    @StateObject private var navigator = IOSNavigator()
    @State private var uiState: PokemonListViewUiState = .loading

    private var useCase: PokemonUseCase {
        KoinHelper.shared.getPokemonUseCase(navigator: navigator)
    }

    private let columns = [
        GridItem(.flexible()),
        GridItem(.flexible()),
        GridItem(.flexible())
    ]

    var body: some View {
        NavigationStack(path: $navigator.path) {
            Group {
                switch uiState {
                case .loading:
                    ProgressView()
                case .success(let pokemonList):
                    ScrollView {
                        LazyVGrid(columns: columns, spacing: 8) {
                            ForEach(pokemonList, id: \.id) { pokemon in
                                Button {
                                    useCase.onTapGrid(pokemonId: pokemon.id)
                                } label: {
                                    PokemonGridItemView(pokemon: pokemon)
                                }
                                .buttonStyle(PlainButtonStyle())
                            }
                        }
                        .padding(8)
                    }
                case .error(let message):
                    VStack {
                        Text(message)
                        Button("再試行") {
                            Task {
                                await loadPokemonList()
                            }
                        }
                    }
                }
            }
            .navigationTitle("ポケモン図鑑")
            .navigationDestination(for: PokemonDetailDestination.self) { destination in
                PokemonDetailView(navigator: navigator, pokemonId: destination.pokemonId)
            }
            .task {
                await loadPokemonList()
            }
        }
    }

    private func loadPokemonList() async {
        uiState = .loading
        do {
            let pokemonList = try await useCase.fetchPokemonList(limit: 50, offset: 0)
            uiState = .success(pokemonList)
        } catch {
            uiState = .error(error.localizedDescription)
        }
    }
}

struct PokemonGridItemView: View {
    let pokemon: PokemonListItem

    var body: some View {
        VStack {
            AsyncImage(url: URL(string: pokemon.spriteUrl ?? "")) { image in
                image
                    .resizable()
                    .aspectRatio(contentMode: .fit)
            } placeholder: {
                ProgressView()
            }
            .frame(width: 80, height: 80)

            Text("#\(pokemon.id)")
                .font(.caption)
            Text(pokemon.name)
                .font(.caption)
                .lineLimit(1)
        }
        .padding(8)
        .background(Color(.systemGray6))
        .cornerRadius(8)
    }
}

struct MenuView: View {
    var body: some View {
        NavigationStack {
            Text("メニュー")
                .navigationTitle("メニュー")
        }
    }
}

struct ContentView_Previews: PreviewProvider {
    static var previews: some View {
        ContentView()
    }
}
