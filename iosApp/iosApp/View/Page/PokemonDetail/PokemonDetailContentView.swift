import SwiftUI
import shared

struct PokemonDetailContentView: View {
    let pokemonDetail: PokemonDetail

    private var primaryTypeColor: Color {
        guard let firstType = pokemonDetail.types.first else {
            return .blue
        }
        return PokemonTypeColors.color(for: firstType)
    }

    var body: some View {
        ScrollView {
            VStack(spacing: 20) {
                headerSection
                typesSection
                physicalSection
                statsSection
                abilitiesSection
            }
            .padding(.bottom, 24)
        }
    }

    private var headerSection: some View {
        ZStack {
            LinearGradient(
                colors: [
                    primaryTypeColor.opacity(0.3),
                    Color.clear
                ],
                startPoint: .top,
                endPoint: .bottom
            )
            .frame(height: 280)

            VStack(spacing: 12) {
                ZStack {
                    Circle()
                        .fill(.white.opacity(0.3))
                        .frame(width: 180, height: 180)

                    AsyncImage(url: URL(string: pokemonDetail.spriteUrl ?? "")) { image in
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fit)
                    } placeholder: {
                        ProgressView()
                    }
                    .frame(width: 160, height: 160)
                }

                Text("#\(String(format: "%03d", pokemonDetail.id))")
                    .font(.subheadline)
                    .foregroundColor(.secondary)

                Text(pokemonDetail.name.capitalized)
                    .font(.title)
                    .fontWeight(.bold)
            }
        }
    }

    private var typesSection: some View {
        HStack(spacing: 8) {
            ForEach(pokemonDetail.types, id: \.self) { type in
                Text(type.capitalized)
                    .font(.subheadline)
                    .fontWeight(.medium)
                    .foregroundColor(.white)
                    .padding(.horizontal, 16)
                    .padding(.vertical, 6)
                    .background(PokemonTypeColors.color(for: type))
                    .clipShape(Capsule())
            }
        }
    }

    private var physicalSection: some View {
        HStack(spacing: 0) {
            VStack(spacing: 4) {
                Text("Height")
                    .font(.caption)
                    .foregroundColor(.secondary)
                let height = pokemonDetail.height?.doubleValue ?? 0
                Text(String(format: "%.1f m", height / 10.0))
                    .font(.headline)
            }
            .frame(maxWidth: .infinity)

            Divider()
                .frame(height: 40)

            VStack(spacing: 4) {
                Text("Weight")
                    .font(.caption)
                    .foregroundColor(.secondary)
                let weight = pokemonDetail.weight?.doubleValue ?? 0
                Text(String(format: "%.1f kg", weight / 10.0))
                    .font(.headline)
            }
            .frame(maxWidth: .infinity)
        }
        .padding(.vertical, 16)
        .background(Color(.secondarySystemBackground))
        .clipShape(RoundedRectangle(cornerRadius: 16))
        .padding(.horizontal, 16)
    }

    private var statsSection: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Base Stats")
                .font(.headline)
                .fontWeight(.bold)
                .padding(.horizontal, 16)

            VStack(spacing: 10) {
                ForEach(pokemonDetail.stats, id: \.name) { stat in
                    statBar(stat: stat)
                }
            }
            .padding(16)
            .background(Color(.secondarySystemBackground))
            .clipShape(RoundedRectangle(cornerRadius: 16))
            .padding(.horizontal, 16)
        }
    }

    private func statBar(stat: PokemonStat) -> some View {
        let statColor = statColor(for: stat.baseStat)

        return HStack(spacing: 8) {
            Text(stat.displayName)
                .font(.caption)
                .fontWeight(.medium)
                .foregroundColor(.secondary)
                .frame(width: 35, alignment: .leading)

            Text("\(stat.baseStat)")
                .font(.subheadline)
                .fontWeight(.bold)
                .frame(width: 35, alignment: .trailing)

            GeometryReader { geometry in
                ZStack(alignment: .leading) {
                    RoundedRectangle(cornerRadius: 4)
                        .fill(Color(.systemGray5))
                        .frame(height: 8)

                    RoundedRectangle(cornerRadius: 4)
                        .fill(statColor)
                        .frame(
                            width: geometry.size.width * CGFloat(min(Double(stat.baseStat) / 255.0, 1.0)),
                            height: 8
                        )
                }
            }
            .frame(height: 8)
        }
    }

    private func statColor(for value: Int32) -> Color {
        switch value {
        case ..<50: return Color(red: 1.0, green: 0.32, blue: 0.32)
        case 50..<80: return Color(red: 1.0, green: 0.72, blue: 0.30)
        case 80..<100: return Color(red: 1.0, green: 0.92, blue: 0.23)
        case 100..<120: return Color(red: 0.61, green: 0.80, blue: 0.40)
        default: return Color(red: 0.40, green: 0.73, blue: 0.42)
        }
    }

    private var abilitiesSection: some View {
        VStack(alignment: .leading, spacing: 12) {
            Text("Abilities")
                .font(.headline)
                .fontWeight(.bold)
                .padding(.horizontal, 16)

            VStack(spacing: 8) {
                ForEach(pokemonDetail.abilities, id: \.name) { ability in
                    HStack {
                        HStack(spacing: 8) {
                            Circle()
                                .fill(ability.isHidden ? Color.secondary : Color.blue)
                                .frame(width: 8, height: 8)

                            Text(ability.name.capitalized)
                                .font(.body)
                        }

                        Spacer()

                        if ability.isHidden {
                            Text("Hidden")
                                .font(.caption)
                                .foregroundColor(.secondary)
                                .padding(.horizontal, 8)
                                .padding(.vertical, 2)
                                .background(Color(.systemGray5))
                                .clipShape(RoundedRectangle(cornerRadius: 4))
                        }
                    }
                }
            }
            .padding(16)
            .background(Color(.secondarySystemBackground))
            .clipShape(RoundedRectangle(cornerRadius: 16))
            .padding(.horizontal, 16)
        }
    }
}
