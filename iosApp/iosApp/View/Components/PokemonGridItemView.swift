import SwiftUI
import shared

struct PokemonGridItemView: View {
    let pokemon: PokemonListItem

    var body: some View {
        VStack {
            AsyncImage(url: URL(string: pokemon.spriteUrl ?? "")) { image in
                image
                    .resizable()
                    .aspectRatio(contentMode: .fit)
            } placeholder: {
                ProgressView()
            }
            .frame(width: 80, height: 80)

            Text("#\(pokemon.id)")
                .font(.caption)
            Text(pokemon.name)
                .font(.caption)
                .lineLimit(1)
        }
        .padding(8)
        .background(Color(.systemGray6))
        .cornerRadius(8)
    }
}
