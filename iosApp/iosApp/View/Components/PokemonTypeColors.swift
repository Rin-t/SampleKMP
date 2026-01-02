import SwiftUI

enum PokemonTypeColors {
    static let normal = Color(red: 0.66, green: 0.66, blue: 0.47)
    static let fire = Color(red: 0.94, green: 0.50, blue: 0.19)
    static let water = Color(red: 0.41, green: 0.56, blue: 0.94)
    static let electric = Color(red: 0.97, green: 0.82, blue: 0.19)
    static let grass = Color(red: 0.47, green: 0.78, blue: 0.31)
    static let ice = Color(red: 0.60, green: 0.85, blue: 0.85)
    static let fighting = Color(red: 0.75, green: 0.19, blue: 0.16)
    static let poison = Color(red: 0.63, green: 0.25, blue: 0.63)
    static let ground = Color(red: 0.88, green: 0.75, blue: 0.41)
    static let flying = Color(red: 0.66, green: 0.56, blue: 0.94)
    static let psychic = Color(red: 0.97, green: 0.35, blue: 0.53)
    static let bug = Color(red: 0.66, green: 0.72, blue: 0.13)
    static let rock = Color(red: 0.72, green: 0.63, blue: 0.22)
    static let ghost = Color(red: 0.44, green: 0.35, blue: 0.60)
    static let dragon = Color(red: 0.44, green: 0.22, blue: 0.97)
    static let dark = Color(red: 0.44, green: 0.35, blue: 0.28)
    static let steel = Color(red: 0.72, green: 0.72, blue: 0.82)
    static let fairy = Color(red: 0.93, green: 0.60, blue: 0.67)

    static func color(for typeName: String) -> Color {
        switch typeName.lowercased() {
        case "normal": return normal
        case "fire": return fire
        case "water": return water
        case "electric": return electric
        case "grass": return grass
        case "ice": return ice
        case "fighting": return fighting
        case "poison": return poison
        case "ground": return ground
        case "flying": return flying
        case "psychic": return psychic
        case "bug": return bug
        case "rock": return rock
        case "ghost": return ghost
        case "dragon": return dragon
        case "dark": return dark
        case "steel": return steel
        case "fairy": return fairy
        default: return normal
        }
    }
}
