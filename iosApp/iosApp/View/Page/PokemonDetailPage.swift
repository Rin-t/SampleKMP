import SwiftUI
import shared

struct PokemonDetailPage: View {
    let navigator: IOSNavigator
    let pokemonId: Int32

    @State private var state: PokemonDetailState = PokemonDetailState()
    @State private var useCase: PokemonDetailUseCase?

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
                            try await useCase?.fetchPokemonDetail()
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
                    useCase?.navigateBack()
                } label: {
                    HStack {
                        Image(systemName: "chevron.left")
                        Text("戻る")
                    }
                }
            }
        }
        .task {
            let uc = KoinHelper.shared.getPokemonDetailUseCase(navigator: navigator, pokemonId: pokemonId)
            useCase = uc

            async let _ = uc.fetchPokemonDetail()

            for await newState in uc.state {
                state = newState
            }
        }
    }
}
