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
        AliceBlue -> R.string.html_color_aliceblue
        AntiqueWhite -> R.string.html_color_antiquewhite
        Aqua -> R.string.html_color_aqua
        Aquamarine -> R.string.html_color_aquamarine
        Azure -> R.string.html_color_azure
        Beige -> R.string.html_color_beige
        Bisque -> R.string.html_color_bisque
        Black -> R.string.html_color_black
        BlanchedAlmond -> R.string.html_color_blanchedalmond
        Blue -> R.string.html_color_blue
        BlueViolet -> R.string.html_color_blueviolet
        Brown -> R.string.html_color_brown
        BurlyWood -> R.string.html_color_burlywood
        CadetBlue -> R.string.html_color_cadetblue
        Chartreuse -> R.string.html_color_chartreuse
        Chocolate -> R.string.html_color_chocolate
        Coral -> R.string.html_color_coral
        CornflowerBlue -> R.string.html_color_cornflowerblue
        Cornsilk -> R.string.html_color_cornsilk
        Crimson -> R.string.html_color_crimson
        Cyan -> R.string.html_color_cyan
        DarkBlue -> R.string.html_color_darkblue
        DarkCyan -> R.string.html_color_darkcyan
        DarkGoldenRod -> R.string.html_color_darkgoldenrod
        DarkGrey -> R.string.html_color_darkgrey
        DarkGreen -> R.string.html_color_darkgreen
        DarkKhaki -> R.string.html_color_darkkhaki
        DarkMagenta -> R.string.html_color_darkmagenta
        DarkOliveGreen -> R.string.html_color_darkolivegreen
        DarkOrange -> R.string.html_color_darkorange
        DarkOrchid -> R.string.html_color_darkorchid
        DarkRed -> R.string.html_color_darkred
        DarkSalmon -> R.string.html_color_darksalmon
        DarkSeaGreen -> R.string.html_color_darkseagreen
        DarkSlateBlue -> R.string.html_color_darkslateblue
        DarkSlateGrey -> R.string.html_color_darkslategrey
        DarkTurquoise -> R.string.html_color_darkturquoise
        DarkViolet -> R.string.html_color_darkviolet
        DeepPink -> R.string.html_color_deeppink
        DeepSkyBlue -> R.string.html_color_deepskyblue
        DimGrey -> R.string.html_color_dimgrey
        DodgerBlue -> R.string.html_color_dodgerblue
        FireBrick -> R.string.html_color_firebrick
        FloralWhite -> R.string.html_color_floralwhite
        ForestGreen -> R.string.html_color_forestgreen
        Fuchsia -> R.string.html_color_fuchsia
        Gainsboro -> R.string.html_color_gainsboro
        GhostWhite -> R.string.html_color_ghostwhite
        Gold -> R.string.html_color_gold
        GoldenRod -> R.string.html_color_goldenrod
        Grey -> R.string.html_color_grey
        Green -> R.string.html_color_green
        GreenYellow -> R.string.html_color_greenyellow
        HoneyDew -> R.string.html_color_honeydew
        HotPink -> R.string.html_color_hotpink
        IndianRed -> R.string.html_color_indianred
        Indigo -> R.string.html_color_indigo
        Ivory -> R.string.html_color_ivory
        Khaki -> R.string.html_color_khaki
        Lavender -> R.string.html_color_lavender
        LavenderBlush -> R.string.html_color_lavenderblush
        LawnGreen -> R.string.html_color_lawngreen
        LemonChiffon -> R.string.html_color_lemonchiffon
        LightBlue -> R.string.html_color_lightblue
        LightCoral -> R.string.html_color_lightcoral
        LightCyan -> R.string.html_color_lightcyan
        LightGoldenRodYellow -> R.string.html_color_lightgoldenrodyellow
        LightGrey -> R.string.html_color_lightgrey
        LightGreen -> R.string.html_color_lightgreen
        LightPink -> R.string.html_color_lightpink
        LightSalmon -> R.string.html_color_lightsalmon
        LightSeaGreen -> R.string.html_color_lightseagreen
        LightSkyBlue -> R.string.html_color_lightskyblue
        LightSlateGrey -> R.string.html_color_lightslategrey
        LightSteelBlue -> R.string.html_color_lightsteelblue
        LightYellow -> R.string.html_color_lightyellow
        Lime -> R.string.html_color_lime
        LimeGreen -> R.string.html_color_limegreen
        Linen -> R.string.html_color_linen
        Magenta -> R.string.html_color_magenta
        Maroon -> R.string.html_color_maroon
        MediumAquaMarine -> R.string.html_color_mediumaquamarine
        MediumBlue -> R.string.html_color_mediumblue
        MediumOrchid -> R.string.html_color_mediumorchid
        MediumPurple -> R.string.html_color_mediumpurple
        MediumSeaGreen -> R.string.html_color_mediumseagreen
        MediumSlateBlue -> R.string.html_color_mediumslateblue
        MediumSpringGreen -> R.string.html_color_mediumspringgreen
        MediumTurquoise -> R.string.html_color_mediumturquoise
        MediumVioletRed -> R.string.html_color_mediumvioletred
        MidnightBlue -> R.string.html_color_midnightblue
        MintCream -> R.string.html_color_mintcream
        MistyRose -> R.string.html_color_mistyrose
        Moccasin -> R.string.html_color_moccasin
        NavajoWhite -> R.string.html_color_navajowhite
        Navy -> R.string.html_color_navy
        OldLace -> R.string.html_color_oldlace
        Olive -> R.string.html_color_olive
        OliveDrab -> R.string.html_color_olivedrab
        Orange -> R.string.html_color_orange
        OrangeRed -> R.string.html_color_orangered
        Orchid -> R.string.html_color_orchid
        PaleGoldenRod -> R.string.html_color_palegoldenrod
        PaleGreen -> R.string.html_color_palegreen
        PaleTurquoise -> R.string.html_color_paleturquoise
        PaleVioletRed -> R.string.html_color_palevioletred
        PapayaWhip -> R.string.html_color_papayawhip
        PeachPuff -> R.string.html_color_peachpuff
        Peru -> R.string.html_color_peru
        Pink -> R.string.html_color_pink
        Plum -> R.string.html_color_plum
        PowderBlue -> R.string.html_color_powderblue
        Purple -> R.string.html_color_purple
        RebeccaPurple -> R.string.html_color_rebeccapurple
        Red -> R.string.html_color_red
        RosyBrown -> R.string.html_color_rosybrown
        RoyalBlue -> R.string.html_color_royalblue
        SaddleBrown -> R.string.html_color_saddlebrown
        Salmon -> R.string.html_color_salmon
        SandyBrown -> R.string.html_color_sandybrown
        SeaGreen -> R.string.html_color_seagreen
        SeaShell -> R.string.html_color_seashell
        Sienna -> R.string.html_color_sienna
        Silver -> R.string.html_color_silver
        SkyBlue -> R.string.html_color_skyblue
        SlateBlue -> R.string.html_color_slateblue
        SlateGrey -> R.string.html_color_slategrey
        Snow -> R.string.html_color_snow
        SpringGreen -> R.string.html_color_springgreen
        SteelBlue -> R.string.html_color_steelblue
        Tan -> R.string.html_color_tan
        Teal -> R.string.html_color_teal
        Thistle -> R.string.html_color_thistle
        Tomato -> R.string.html_color_tomato
        Turquoise -> R.string.html_color_turquoise
        Violet -> R.string.html_color_violet
        Wheat -> R.string.html_color_wheat
        White -> R.string.html_color_white
        WhiteSmoke -> R.string.html_color_whitesmoke
        Yellow -> R.string.html_color_yellow
        YellowGreen -> R.string.html_color_yellowgreen
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
