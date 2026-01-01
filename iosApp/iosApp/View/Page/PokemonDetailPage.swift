import SwiftUI
import shared

enum PokemonDetailPageUiState {
    case loading
    case success(PokemonDetail)
    case error(String)
}

struct PokemonDetailPage: View {
    let navigator: IOSNavigator
    let pokemonId: Int32

    @State private var uiState: PokemonDetailPageUiState = .loading

    private var useCase: PokemonDetailUseCase {
        KoinHelper.shared.getPokemonDetailUseCase(navigator: navigator, pokemonId: pokemonId)
    }

    var body: some View {
        Group {
            switch uiState {
            case .loading:
                ProgressView()
            case .success(let pokemonDetail):
                PokemonDetailContentView(pokemonDetail: pokemonDetail)
            case .error(let message):
                VStack {
                    Text(message)
                    Button("再試行") {
                        Task {
                            await loadPokemonDetail()
                        }
                    }
                }
            }
        }
        .navigationTitle("詳細")
        .navigationBarBackButtonHidden(true)
        .toolbar {
            ToolbarItem(placement: .navigationBarLeading) {
                Button {
                    useCase.navigateBack()
                } label: {
                    HStack {
                        Image(systemName: "chevron.left")
                        Text("戻る")
                    }
                }
            }
        }
        .task {
            await loadPokemonDetail()
        }
    }

    private func loadPokemonDetail() async {
        uiState = .loading
        do {
            if let detail = try await useCase.fetchPokemonDetail() {
                uiState = .success(detail)
            } else {
                uiState = .error("Pokemon not found")
            }
        } catch {
            uiState = .error(error.localizedDescription)
        }
    }
}
