import SwiftUI
import shared

struct PokemonGridItem: View {
    let pokemon: PokemonListItem

    var body: some View {
        VStack(spacing: 0) {
            ZStack {
                LinearGradient(
                    colors: [
                        Color.blue.opacity(0.15),
                        Color(.systemBackground)
                    ],
                    startPoint: .top,
                    endPoint: .bottom
                )

                AsyncImage(url: URL(string: pokemon.spriteUrl ?? "")) { image in
                    image
                        .resizable()
                        .aspectRatio(contentMode: .fit)
                } placeholder: {
                    ProgressView()
                }
                .frame(width: 70, height: 70)
            }
            .frame(height: 80)

            VStack(spacing: 2) {
                Text("#\(String(format: "%03d", pokemon.id))")
                    .font(.caption2)
                    .foregroundColor(.secondary)
                Text(pokemon.name.capitalized)
                    .font(.caption)
                    .fontWeight(.medium)
                    .lineLimit(1)
            }
            .padding(.horizontal, 4)
            .padding(.vertical, 8)
        }
        .background(Color(.systemBackground))
        .clipShape(RoundedRectangle(cornerRadius: 16))
        .shadow(color: .black.opacity(0.1), radius: 4, x: 0, y: 2)
    }
}
