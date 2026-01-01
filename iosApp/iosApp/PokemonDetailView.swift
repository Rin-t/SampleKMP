import SwiftUI
import shared

enum PokemonDetailViewUiState {
    case loading
    case success(PokemonDetail)
    case error(String)
}

struct PokemonDetailView: View {
    let navigator: IOSNavigator
    let pokemonId: Int32

    @State private var uiState: PokemonDetailViewUiState = .loading

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

struct PokemonDetailContentView: View {
    let pokemonDetail: PokemonDetail

    var body: some View {
        ScrollView {
            VStack(spacing: 16) {
                // Header with image
                AsyncImage(url: URL(string: pokemonDetail.spriteUrl ?? "")) { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                } placeholder: {
                    ProgressView()
                }
                .frame(width: 200, height: 200)

                Text("#\(pokemonDetail.id) \(pokemonDetail.name)")
                    .font(.title)

                // Types
                HStack(spacing: 8) {
                    ForEach(pokemonDetail.types, id: \.self) { type in
                        Text(type)
                            .padding(.horizontal, 12)
                            .padding(.vertical, 4)
                            .background(Color(.systemGray5))
                            .cornerRadius(8)
                    }
                }

                // Height & Weight
                HStack(spacing: 40) {
                    VStack {
                        Text("Height")
                            .font(.caption)
                        let height = pokemonDetail.height?.doubleValue ?? 0
                        Text(String(format: "%.1f m", height / 10.0))
                    }
                    VStack {
                        Text("Weight")
                            .font(.caption)
                        let weight = pokemonDetail.weight?.doubleValue ?? 0
                        Text(String(format: "%.1f kg", weight / 10.0))
                    }
                }

                // Stats
                VStack(alignment: .leading, spacing: 8) {
                    Text("Base Stats")
                        .font(.headline)

                    ForEach(pokemonDetail.stats, id: \.name) { stat in
                        StatRowView(statName: stat.name, statValue: Int(stat.baseStat))
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal)

                // Abilities
                VStack(alignment: .leading, spacing: 8) {
                    Text("Abilities")
                        .font(.headline)

                    ForEach(pokemonDetail.abilities, id: \.name) { ability in
                        HStack {
                            Text(ability.name)
                            Spacer()
                            if ability.isHidden {
                                Text("(Hidden)")
                                    .font(.caption)
                                    .foregroundColor(.secondary)
                            }
                        }
                    }
                }
                .frame(maxWidth: .infinity, alignment: .leading)
                .padding(.horizontal)
            }
            .padding()
        }
    }
}

struct StatRowView: View {
    let statName: String
    let statValue: Int

    var body: some View {
        HStack {
            Text(statName)
                .frame(width: 100, alignment: .leading)
                .font(.caption)
            Text("\(statValue)")
                .frame(width: 40)
            ProgressView(value: Double(statValue) / 255.0)
        }
    }
}
