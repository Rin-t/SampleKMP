import SwiftUI

struct TabPage: View {
    var body: some View {
        TabView {
            PokemonListPage()
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
    }
}

struct TabPage_Previews: PreviewProvider {
    static var previews: some View {
        TabPage()
    }
}
