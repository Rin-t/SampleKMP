import SwiftUI
import shared

struct PokemonDetailPage: View {
    let navigator: IOSNavigator
    let pokemonId: Int32

    @State private var uiState: PokemonDetailUiState = PokemonDetailUiState.Loading()

    private var useCase: PokemonDetailUseCase {
        KoinHelper.shared.getPokemonDetailUseCase(navigator: navigator, pokemonId: pokemonId)
    }

    var body: some View {
        Group {
            switch onEnum(of: uiState) {
            case .loading:
                ProgressView()
            case .success(let success):
                PokemonDetailContentView(pokemonDetail: success.pokemonDetail)
            case .error(let error):
                VStack {
                    Text(error.message)
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
        uiState = PokemonDetailUiState.Loading()
        do {
            if let detail = try await useCase.fetchPokemonDetail() {
                uiState = PokemonDetailUiState.Success(pokemonDetail: detail)
            } else {
                uiState = PokemonDetailUiState.Error(message: "Pokemon not found")
            }
        } catch {
            uiState = PokemonDetailUiState.Error(message: error.localizedDescription)
        }
    }
}
