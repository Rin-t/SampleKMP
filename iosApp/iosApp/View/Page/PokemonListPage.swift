import SwiftUI
import shared

struct PokemonListPage: View {
    @State private var navigator = IOSNavigator()
    @State private var state: PokemonListState = PokemonListState()
    @State private var useCase: PokemonUseCase?

    private let columns = [
        GridItem(.flexible()),
        GridItem(.flexible()),
        GridItem(.flexible())
    ]

    var body: some View {
        NavigationStack(path: $navigator.path) {
            Group {
                switch onEnum(of: state.status) {
                case .fetching:
                    ProgressView()
                case .success:
                    ScrollView {
                        LazyVGrid(columns: columns, spacing: 8) {
                            ForEach(state.pokemonList, id: \.id) { pokemon in
                                Button {
                                    useCase?.onTapGrid(pokemonId: pokemon.id)
                                } label: {
                                    PokemonGridItemView(pokemon: pokemon)
                                }
                                .buttonStyle(PlainButtonStyle())
                            }
                        }
                        .padding(8)
                    }
                case .failed(let failed):
                    VStack {
                        Text(failed.message)
                        Button("再試行") {
                            Task {
                                try await useCase?.fetchPokemonList(limit: 50, offset: 0)
                            }
                        }
                    }
                }
            }
            .navigationTitle("ポケモン図鑑")
            .navigationBarTitleDisplayMode(.inline)
            .withAppRouter(navigator: navigator)
            .task {
                let uc = KoinHelper.shared.getPokemonUseCase(navigator: navigator)
                useCase = uc

                async let _ = uc.fetchPokemonList(limit: 50, offset: 0)

                for await newState in uc.state {
                    state = newState
                }
            }
        }
    }
}
