import SwiftUI
import shared

struct PokemonDetailPage: View {
    let useCase: PokemonDetailUseCase
    @State private var state: PokemonDetailState = PokemonDetailState()

    var body: some View {
        Group {
            switch onEnum(of: state.status) {
            case .fetching:
                ProgressView()
            case .success:
                if let detail = state.pokemonDetail {
                    PokemonDetailContentView(pokemonDetail: detail)
                }
            case .failed(let failed):
                VStack {
                    Text(failed.message)
                    Button("再試行") {
                        Task {
                            try await useCase.onAppear()
                        }
                    }
                }
            }
        }
        .navigationTitle("ポケモン詳細")
        .navigationBarTitleDisplayMode(.inline)
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
