import SwiftUI
import shared

struct PokemonDetailContentView: View {
    let pokemonDetail: PokemonDetail

    var body: some View {
        ScrollView {
            VStack(spacing: 16) {
                headerSection
                typesSection
                physicalSection
                statsSection
                abilitiesSection
            }
            .padding()
        }
    }

    private var headerSection: some View {
        VStack {
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
        }
    }

    private var typesSection: some View {
        HStack(spacing: 8) {
            ForEach(pokemonDetail.types, id: \.self) { type in
                Text(type)
                    .padding(.horizontal, 12)
                    .padding(.vertical, 4)
                    .background(Color(.systemGray5))
                    .cornerRadius(8)
            }
        }
    }

    private var physicalSection: some View {
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
    }

    private var statsSection: some View {
        VStack(alignment: .leading, spacing: 8) {
            Text("Base Stats")
                .font(.headline)

            ForEach(pokemonDetail.stats, id: \.name) { stat in
                HStack {
                    Text(stat.name)
                        .frame(width: 100, alignment: .leading)
                        .font(.caption)
                    Text("\(stat.baseStat)")
                        .frame(width: 40)
                    ProgressView(value: Double(stat.baseStat) / 255.0)
                }
            }
        }
        .frame(maxWidth: .infinity, alignment: .leading)
        .padding(.horizontal)
    }

    private var abilitiesSection: some View {
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
}
