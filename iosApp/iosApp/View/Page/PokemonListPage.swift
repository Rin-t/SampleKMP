import SwiftUI
import shared

struct PokemonListPage: View {
    @StateObject private var navigator = IOSNavigator()
    @State private var uiState: PokemonListUiState = PokemonListUiState.Loading()

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
                switch onEnum(of: uiState) {
                case .loading:
                    ProgressView()
                case .success(let success):
                    ScrollView {
                        LazyVGrid(columns: columns, spacing: 8) {
                            ForEach(success.pokemonList, id: \.id) { pokemon in
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
                case .error(let error):
                    VStack {
                        Text(error.message)
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
        uiState = PokemonListUiState.Loading()
        do {
            let pokemonList = try await useCase.fetchPokemonList(limit: 50, offset: 0)
            uiState = PokemonListUiState.Success(pokemonList: pokemonList)
        } catch {
            uiState = PokemonListUiState.Error(message: error.localizedDescription)
        }
    }
}
