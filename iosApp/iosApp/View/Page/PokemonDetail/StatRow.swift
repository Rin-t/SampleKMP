import SwiftUI

struct StatRow: View {
    let statName: String
    let statValue: Int

    var body: some View {
        HStack {
            Text(statName)
                .frame(width: 100, alignment: .leading)
                .font(.caption)
            Text("\(statValue)")
                .frame(width: 40)
            ProgressView(value: Double(statValue) / 255.0)
        }
    }
}
