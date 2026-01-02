import SwiftUI
import shared

struct TabPage: View {
    @State private var navigator = IOSNavigator()

    var body: some View {
        TabView {
            PokemonListPage(
                useCase: KoinProvider.shared.getPokemonUseCase(navigator: navigator)
            )
            .tabItem {
                Image(systemName: "list.bullet")
                Text("図鑑")
            }

            MenuPage()
                .tabItem {
                    Image(systemName: "line.3.horizontal")
                    Text("メニュー")
                }
        }
        .environment(navigator)
    }
}

struct TabPage_Previews: PreviewProvider {
    static var previews: some View {
        TabPage()
    }
}
