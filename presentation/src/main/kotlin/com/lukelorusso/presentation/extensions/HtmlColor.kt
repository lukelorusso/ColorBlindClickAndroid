package com.lukelorusso.presentation.extensions

import androidx.annotation.StringRes
import com.lukelorusso.presentation.R

enum class HtmlColor(val hex: String) {
    AliceBlue("#F0F8FF"),
    AntiqueWhite("#FAEBD7"),
    Aqua("#00FFFF"),
    Aquamarine("#7FFFD4"),
    Azure("#F0FFFF"),
    Beige("#F5F5DC"),
    Bisque("#FFE4C4"),
    Black("#000000"),
    BlanchedAlmond("#FFEBCD"),
    Blue("#0000FF"),
    BlueViolet("#8A2BE2"),
    Brown("#A52A2A"),
    BurlyWood("#DEB887"),
    CadetBlue("#5F9EA0"),
    Chartreuse("#7FFF00"),
    Chocolate("#D2691E"),
    Coral("#FF7F50"),
    CornflowerBlue("#6495ED"),
    Cornsilk("#FFF8DC"),
    Crimson("#DC143C"),
    Cyan("#00FFFF"),
    DarkBlue("#00008B"),
    DarkCyan("#008B8B"),
    DarkGoldenRod("#B8860B"),
    DarkGrey("#A9A9A9"),
    DarkGreen("#006400"),
    DarkKhaki("#BDB76B"),
    DarkMagenta("#8B008B"),
    DarkOliveGreen("#556B2F"),
    DarkOrange("#FF8C00"),
    DarkOrchid("#9932CC"),
    DarkRed("#8B0000"),
    DarkSalmon("#E9967A"),
    DarkSeaGreen("#8FBC8F"),
    DarkSlateBlue("#483D8B"),
    DarkSlateGrey("#2F4F4F"),
    DarkTurquoise("#00CED1"),
    DarkViolet("#9400D3"),
    DeepPink("#FF1493"),
    DeepSkyBlue("#00BFFF"),
    DimGrey("#696969"),
    DodgerBlue("#1E90FF"),
    FireBrick("#B22222"),
    FloralWhite("#FFFAF0"),
    ForestGreen("#228B22"),
    Fuchsia("#FF00FF"),
    Gainsboro("#DCDCDC"),
    GhostWhite("#F8F8FF"),
    Gold("#FFD700"),
    GoldenRod("#DAA520"),
    Grey("#808080"),
    Green("#008000"),
    GreenYellow("#ADFF2F"),
    HoneyDew("#F0FFF0"),
    HotPink("#FF69B4"),
    IndianRed("#CD5C5C"),
    Indigo("#4B0082"),
    Ivory("#FFFFF0"),
    Khaki("#F0E68C"),
    Lavender("#E6E6FA"),
    LavenderBlush("#FFF0F5"),
    LawnGreen("#7CFC00"),
    LemonChiffon("#FFFACD"),
    LightBlue("#ADD8E6"),
    LightCoral("#F08080"),
    LightCyan("#E0FFFF"),
    LightGoldenRodYellow("#FAFAD2"),
    LightGrey("#D3D3D3"),
    LightGreen("#90EE90"),
    LightPink("#FFB6C1"),
    LightSalmon("#FFA07A"),
    LightSeaGreen("#20B2AA"),
    LightSkyBlue("#87CEFA"),
    LightSlateGrey("#778899"),
    LightSteelBlue("#B0C4DE"),
    LightYellow("#FFFFE0"),
    Lime("#00FF00"),
    LimeGreen("#32CD32"),
    Linen("#FAF0E6"),
    Magenta("#FF00FF"),
    Maroon("#800000"),
    MediumAquaMarine("#66CDAA"),
    MediumBlue("#0000CD"),
    MediumOrchid("#BA55D3"),
    MediumPurple("#9370DB"),
    MediumSeaGreen("#3CB371"),
    MediumSlateBlue("#7B68EE"),
    MediumSpringGreen("#00FA9A"),
    MediumTurquoise("#48D1CC"),
    MediumVioletRed("#C71585"),
    MidnightBlue("#191970"),
    MintCream("#F5FFFA"),
    MistyRose("#FFE4E1"),
    Moccasin("#FFE4B5"),
    NavajoWhite("#FFDEAD"),
    Navy("#000080"),
    OldLace("#FDF5E6"),
    Olive("#808000"),
    OliveDrab("#6B8E23"),
    Orange("#FFA500"),
    OrangeRed("#FF4500"),
    Orchid("#DA70D6"),
    PaleGoldenRod("#EEE8AA"),
    PaleGreen("#98FB98"),
    PaleTurquoise("#AFEEEE"),
    PaleVioletRed("#DB7093"),
    PapayaWhip("#FFEFD5"),
    PeachPuff("#FFDAB9"),
    Peru("#CD853F"),
    Pink("#FFC0CB"),
    Plum("#DDA0DD"),
    PowderBlue("#B0E0E6"),
    Purple("#800080"),
    RebeccaPurple("#663399"),
    Red("#FF0000"),
    RosyBrown("#BC8F8F"),
    RoyalBlue("#4169E1"),
    SaddleBrown("#8B4513"),
    Salmon("#FA8072"),
    SandyBrown("#F4A460"),
    SeaGreen("#2E8B57"),
    SeaShell("#FFF5EE"),
    Sienna("#A0522D"),
    Silver("#C0C0C0"),
    SkyBlue("#87CEEB"),
    SlateBlue("#6A5ACD"),
    SlateGrey("#708090"),
    Snow("#FFFAFA"),
    SpringGreen("#00FF7F"),
    SteelBlue("#4682B4"),
    Tan("#D2B48C"),
    Teal("#008080"),
    Thistle("#D8BFD8"),
    Tomato("#FF6347"),
    Turquoise("#40E0D0"),
    Violet("#EE82EE"),
    Wheat("#F5DEB3"),
    White("#FFFFFF"),
    WhiteSmoke("#F5F5F5"),
    Yellow("#FFFF00"),
    YellowGreen("#9ACD32");

    @StringRes
    fun resId(): Int = when (this) {
        AliceBlue -> R.string.basic_color_aliceblue
        AntiqueWhite -> R.string.basic_color_antiquewhite
        Aqua -> R.string.basic_color_aqua
        Aquamarine -> R.string.basic_color_aquamarine
        Azure -> R.string.basic_color_azure
        Beige -> R.string.basic_color_beige
        Bisque -> R.string.basic_color_bisque
        Black -> R.string.basic_color_black
        BlanchedAlmond -> R.string.basic_color_blanchedalmond
        Blue -> R.string.basic_color_blue
        BlueViolet -> R.string.basic_color_blueviolet
        Brown -> R.string.basic_color_brown
        BurlyWood -> R.string.basic_color_burlywood
        CadetBlue -> R.string.basic_color_cadetblue
        Chartreuse -> R.string.basic_color_chartreuse
        Chocolate -> R.string.basic_color_chocolate
        Coral -> R.string.basic_color_coral
        CornflowerBlue -> R.string.basic_color_cornflowerblue
        Cornsilk -> R.string.basic_color_cornsilk
        Crimson -> R.string.basic_color_crimson
        Cyan -> R.string.basic_color_cyan
        DarkBlue -> R.string.basic_color_darkblue
        DarkCyan -> R.string.basic_color_darkcyan
        DarkGoldenRod -> R.string.basic_color_darkgoldenrod
        DarkGrey -> R.string.basic_color_darkgrey
        DarkGreen -> R.string.basic_color_darkgreen
        DarkKhaki -> R.string.basic_color_darkkhaki
        DarkMagenta -> R.string.basic_color_darkmagenta
        DarkOliveGreen -> R.string.basic_color_darkolivegreen
        DarkOrange -> R.string.basic_color_darkorange
        DarkOrchid -> R.string.basic_color_darkorchid
        DarkRed -> R.string.basic_color_darkred
        DarkSalmon -> R.string.basic_color_darksalmon
        DarkSeaGreen -> R.string.basic_color_darkseagreen
        DarkSlateBlue -> R.string.basic_color_darkslateblue
        DarkSlateGrey -> R.string.basic_color_darkslategrey
        DarkTurquoise -> R.string.basic_color_darkturquoise
        DarkViolet -> R.string.basic_color_darkviolet
        DeepPink -> R.string.basic_color_deeppink
        DeepSkyBlue -> R.string.basic_color_deepskyblue
        DimGrey -> R.string.basic_color_dimgrey
        DodgerBlue -> R.string.basic_color_dodgerblue
        FireBrick -> R.string.basic_color_firebrick
        FloralWhite -> R.string.basic_color_floralwhite
        ForestGreen -> R.string.basic_color_forestgreen
        Fuchsia -> R.string.basic_color_fuchsia
        Gainsboro -> R.string.basic_color_gainsboro
        GhostWhite -> R.string.basic_color_ghostwhite
        Gold -> R.string.basic_color_gold
        GoldenRod -> R.string.basic_color_goldenrod
        Grey -> R.string.basic_color_grey
        Green -> R.string.basic_color_green
        GreenYellow -> R.string.basic_color_greenyellow
        HoneyDew -> R.string.basic_color_honeydew
        HotPink -> R.string.basic_color_hotpink
        IndianRed -> R.string.basic_color_indianred
        Indigo -> R.string.basic_color_indigo
        Ivory -> R.string.basic_color_ivory
        Khaki -> R.string.basic_color_khaki
        Lavender -> R.string.basic_color_lavender
        LavenderBlush -> R.string.basic_color_lavenderblush
        LawnGreen -> R.string.basic_color_lawngreen
        LemonChiffon -> R.string.basic_color_lemonchiffon
        LightBlue -> R.string.basic_color_lightblue
        LightCoral -> R.string.basic_color_lightcoral
        LightCyan -> R.string.basic_color_lightcyan
        LightGoldenRodYellow -> R.string.basic_color_lightgoldenrodyellow
        LightGrey -> R.string.basic_color_lightgrey
        LightGreen -> R.string.basic_color_lightgreen
        LightPink -> R.string.basic_color_lightpink
        LightSalmon -> R.string.basic_color_lightsalmon
        LightSeaGreen -> R.string.basic_color_lightseagreen
        LightSkyBlue -> R.string.basic_color_lightskyblue
        LightSlateGrey -> R.string.basic_color_lightslategrey
        LightSteelBlue -> R.string.basic_color_lightsteelblue
        LightYellow -> R.string.basic_color_lightyellow
        Lime -> R.string.basic_color_lime
        LimeGreen -> R.string.basic_color_limegreen
        Linen -> R.string.basic_color_linen
        Magenta -> R.string.basic_color_magenta
        Maroon -> R.string.basic_color_maroon
        MediumAquaMarine -> R.string.basic_color_mediumaquamarine
        MediumBlue -> R.string.basic_color_mediumblue
        MediumOrchid -> R.string.basic_color_mediumorchid
        MediumPurple -> R.string.basic_color_mediumpurple
        MediumSeaGreen -> R.string.basic_color_mediumseagreen
        MediumSlateBlue -> R.string.basic_color_mediumslateblue
        MediumSpringGreen -> R.string.basic_color_mediumspringgreen
        MediumTurquoise -> R.string.basic_color_mediumturquoise
        MediumVioletRed -> R.string.basic_color_mediumvioletred
        MidnightBlue -> R.string.basic_color_midnightblue
        MintCream -> R.string.basic_color_mintcream
        MistyRose -> R.string.basic_color_mistyrose
        Moccasin -> R.string.basic_color_moccasin
        NavajoWhite -> R.string.basic_color_navajowhite
        Navy -> R.string.basic_color_navy
        OldLace -> R.string.basic_color_oldlace
        Olive -> R.string.basic_color_olive
        OliveDrab -> R.string.basic_color_olivedrab
        Orange -> R.string.basic_color_orange
        OrangeRed -> R.string.basic_color_orangered
        Orchid -> R.string.basic_color_orchid
        PaleGoldenRod -> R.string.basic_color_palegoldenrod
        PaleGreen -> R.string.basic_color_palegreen
        PaleTurquoise -> R.string.basic_color_paleturquoise
        PaleVioletRed -> R.string.basic_color_palevioletred
        PapayaWhip -> R.string.basic_color_papayawhip
        PeachPuff -> R.string.basic_color_peachpuff
        Peru -> R.string.basic_color_peru
        Pink -> R.string.basic_color_pink
        Plum -> R.string.basic_color_plum
        PowderBlue -> R.string.basic_color_powderblue
        Purple -> R.string.basic_color_purple
        RebeccaPurple -> R.string.basic_color_rebeccapurple
        Red -> R.string.basic_color_red
        RosyBrown -> R.string.basic_color_rosybrown
        RoyalBlue -> R.string.basic_color_royalblue
        SaddleBrown -> R.string.basic_color_saddlebrown
        Salmon -> R.string.basic_color_salmon
        SandyBrown -> R.string.basic_color_sandybrown
        SeaGreen -> R.string.basic_color_seagreen
        SeaShell -> R.string.basic_color_seashell
        Sienna -> R.string.basic_color_sienna
        Silver -> R.string.basic_color_silver
        SkyBlue -> R.string.basic_color_skyblue
        SlateBlue -> R.string.basic_color_slateblue
        SlateGrey -> R.string.basic_color_slategrey
        Snow -> R.string.basic_color_snow
        SpringGreen -> R.string.basic_color_springgreen
        SteelBlue -> R.string.basic_color_steelblue
        Tan -> R.string.basic_color_tan
        Teal -> R.string.basic_color_teal
        Thistle -> R.string.basic_color_thistle
        Tomato -> R.string.basic_color_tomato
        Turquoise -> R.string.basic_color_turquoise
        Violet -> R.string.basic_color_violet
        Wheat -> R.string.basic_color_wheat
        White -> R.string.basic_color_white
        WhiteSmoke -> R.string.basic_color_whitesmoke
        Yellow -> R.string.basic_color_yellow
        YellowGreen -> R.string.basic_color_yellowgreen
    }
}

/**
 * RGB: 93, 54, 47 -> Maroon
 */
fun Triple<Int, Int, Int>.closestHtmlColor(): HtmlColor {
    val lab = this.toLabDouble()
    val distanceMap = HtmlColor.entries.associateWith { basicColor ->
        val labBasicColor = basicColor.hex.hashColorToRGBDecimal().toLabDouble()
        CIEDE2000.calculateDeltaE(
            lab.first, lab.second, lab.third,
            labBasicColor.first, labBasicColor.second, labBasicColor.third
        )
    }
    return distanceMap.entries.minBy { it.value }.key
}
