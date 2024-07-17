import SwiftUI
import shared

struct ContentView: View {
    
    @StateObject var viewModel = ViewModel()
    @State var textFieldInput = ""

	var body: some View {
        VStack {
            TextField("1~151の数字を入力", text: $textFieldInput)
                .padding(.horizontal, 32)
                .padding(.bottom, 24)
            
            Button {
                let id = Int32(textFieldInput) ?? 1
                viewModel.tappedSearchButton(id: id)
            } label: {
                Text("Search")
            }
            
            AsyncImage(url: viewModel.imageURL)
                .frame(width: 200, height: 200)
                .foregroundStyle(.white)
            Text(viewModel.pokemon?.name ?? "")
        }
	}
}

struct ContentView_Previews: PreviewProvider {
	static var previews: some View {
		ContentView()
	}
}

@MainActor
final class ViewModel: ObservableObject {
    @Published var pokemon: Pokemon?
    var imageURL: URL? {
        guard let pokemon else { return nil }
        return URL(string: pokemon.sprities.normal)!
    }
    
    let useCase = PokemonUseCase()

    func tappedSearchButton(id: Int32) {
        Task {
            do {
                pokemon = try await useCase.fetchPokemon(id: id)
            } catch {
                print("error")
            }
        }
    }
}
