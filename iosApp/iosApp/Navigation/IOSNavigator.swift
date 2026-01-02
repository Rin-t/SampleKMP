import SwiftUI
import shared

// MARK: - Router Destination

enum RouterDestination: Hashable {
    case pokemonDetail(Int32)
}

// MARK: - App Router

struct AppRouter: ViewModifier {
    @Environment(IOSNavigator.self) private var navigator

    func body(content: Content) -> some View {
        content
            .navigationDestination(for: RouterDestination.self) { destination in
                switch destination {
                case .pokemonDetail(let pokemonId):
                    let useCase = KoinProvider.shared.getPokemonDetailUseCase(
                        navigator: navigator,
                        pokemonId: pokemonId
                    )
                    PokemonDetailPage(useCase: useCase)
                }
            }
    }
}

extension View {
    func withAppRouter() -> some View {
        modifier(AppRouter())
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
