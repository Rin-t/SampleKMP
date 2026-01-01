import SwiftUI
import shared

class IOSNavigator: ObservableObject, Navigator {
    @Published var path = NavigationPath()

    func navigate(destination: Destination) {
        switch destination {
        case let detail as Destination.PokemonDetail:
            path.append(PokemonDetailDestination(pokemonId: detail.pokemonId))
        default:
            break
        }
    }

    func navigateBack() {
        if !path.isEmpty {
            path.removeLast()
        }
    }
}

struct PokemonDetailDestination: Hashable {
    let pokemonId: Int32
}
