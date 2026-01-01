import SwiftUI
import shared

enum PokemonListPageUiState {
    case loading
    case success([PokemonListItem])
    case error(String)
}

struct PokemonListPage: View {
    @StateObject private var navigator = IOSNavigator()
    @State private var uiState: PokemonListPageUiState = .loading

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
                PokemonDetailPage(navigator: navigator, pokemonId: destination.pokemonId)
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
