import SwiftUI
import shared

struct PokemonListPage: View {
    @Environment(IOSNavigator.self) private var navigator
    let useCase: PokemonUseCase
    @State private var state: PokemonListState = PokemonListState()

    private let columns = [
        GridItem(.flexible()),
        GridItem(.flexible()),
        GridItem(.flexible())
    ]

    var body: some View {
        @Bindable var navigator = navigator
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
                                    useCase.onTapGrid(pokemonId: pokemon.id)
                                } label: {
                                    PokemonGridItem(pokemon: pokemon)
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
                                try await useCase.onTapRetryButton()
                            }
                        }
                    }
                }
            }
            .navigationTitle("ポケモン図鑑")
            .navigationBarTitleDisplayMode(.inline)
            .withAppRouter()
            .task {
                try? await useCase.onAppear()
            }
            .task {
                for await newState in useCase.state {
                    state = newState
                }
            }
        }
    }
}
