import SwiftUI
import shared

// MARK: - Router Destination

enum RouterDestination: Hashable {
    case pokemonDetail(Int32)
}

// MARK: - App Router

struct AppRouter: ViewModifier {
    let navigator: IOSNavigator

    func body(content: Content) -> some View {
        content
            .navigationDestination(for: RouterDestination.self) { destination in
                switch destination {
                case .pokemonDetail(let pokemonId):
                    let useCase = KoinHelper.shared.getPokemonDetailUseCase(
                        navigator: navigator,
                        pokemonId: pokemonId
                    )
                    PokemonDetailPage(useCase: useCase)
                }
            }
    }
}

extension View {
    func withAppRouter(navigator: IOSNavigator) -> some View {
        modifier(AppRouter(navigator: navigator))
    }
}

// MARK: - iOS Navigator

@Observable
class IOSNavigator: Navigator {
    var path = NavigationPath()

    func navigate(destination: Destination) {
        switch destination {
        case let detail as Destination.PokemonDetail:
            path.append(RouterDestination.pokemonDetail(detail.pokemonId))
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
