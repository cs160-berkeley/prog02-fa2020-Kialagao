package com.gmail.kingarthuralagao.us.represent

object AlaskaBoundaries {
    const val westernMost = -167.0362
    const val easternMost = -141.3502
    const val northernMost = 71.36516
    const val southernMost = 51.214183
}

object HawaiiBoundaries {
    const val westernMost = -160.2452
    const val easternMost = -154.806773
    const val northernMost = 22.2284
    const val southernMost = 18.9138
}

object MainBoundaries {
    const val westernMost = -124.7844079
    const val easternMost = -66.9513812
    const val northernMost = 49.3457868
    const val southernMost = 24.7433195
}

val a = Rectangle(48.1384, -122.3676, -123.6416, 38.9347)
val b = Rectangle(48.9178, -120.4385, -122.3677, 37.7408)
val c = Rectangle(48.9178, -117.1553, -120.4386, 34.5634)
val d = Rectangle(47.9588, -87.6378, -117.1554, 32.7153)
val e = Rectangle(45.5438, -83.1828, -87.6379, 30.4944)
val f = Rectangle(41.6280, -81.5762, -83.1829, 30.0876)
val g = Rectangle(29.9004, -81.3214, -82.5999, 27.4776)
val h = Rectangle(28.0838,  -80.5938, -81.7690, 26.1495)
val i = Rectangle(41.5807, -75.9973, -81.3215, 35.4610)
val j = Rectangle(44.9039, -70.9540, -74.9357, 41.4273)
val k = Rectangle(46.3606, -67.8306, -69.9867, 44.6354)
val l = Rectangle(69.4838, -141.4149, -160.4612, 60.7969)
val m = Rectangle(19.8895, -155.1794, -155.8589, 19.2861)
val n = Rectangle(20.8392, -156.1443, -156.4162,  20.6362)
val o = Rectangle(21.5495, -157.8693, -158.1082, 21.3245)

data class Rectangle(val n : Double, val e : Double, val w: Double, val s : Double)

fun getRectangles() : MutableList<Rectangle>{
    return mutableListOf(a, b, c, d, e, f, g, h, i, j, k, l, m, n, o)
}
/****** List of rectangles from left to right******/
// bottom left : 38.9347, -123.6416
// upper right: 48.1384, -122.3676
// Area: 11.725513800000005
// Percentage: 1.4

// bottom left: 37.7408, -122.3677
// upper right: 48.9178, -120.4385
// Area: 21.562668399999936
// Percentage: 2.6

// bottom left: 34.5634, -120.4386
// upper right: 48.9178, -117.1553
// Area: 47.12980151999995
// Percentage: 5.6

// Center box
// bottom left: 32.7153, -117.1554
// upper right: 47.9588, -87.6378
// Area: 449.95153559999994
// Percentage: 53.7

// bottom left: 30.4944, -87.6379
// upper right: 45.5438, -83.1828
// Area: 67.04658194000002
// Percentage: 8

// bottom left: 30.0876, -83.1829
// upper right: 41.6280, -81.5762
// Area: 18.541960680000045
// Percentage: 2.2

// bottom left: 27.4776, -82.5999
// upper right: 29.9004, -81.3214
// Area: 3.0975498000000226
// Percentage: 0.4

// bottom left: 26.1495, -81.7690
// upper right: 28.0838, -80.5938
// Area: 2.273189360000008
// Percentage: 0.3

// bottom left: 35.4610, -81.3215
// upper right: 41.5807, -75.9973
// Area: 32.582506740000035
// Percentage: 3.9

// btm left: 41.4273, -74.9357
// upper right: 44.9039, -70.9540
// Area: 13.842778220000003
// Percentage: 1.7

// bottom left: 44.6354, -69.9867
// upper right: 46.3606, -67.8306
// Area: 3.7197037199999934
// Percentage: 0.4

// Alaska
// bottom left: 60.7969, -160.4612
// upper right: 69.4838, -141.4149
// Area: 165.45330347000004
// Percentage: 19.8

// Hawaii
// bottom left: 19.2861, -155.8589
// upper right: 19.8895, -155.1794
// Area: 0.41001030000001165
// Percentage: 0.05

// bottom left: 20.6362, -156.4162
// upper right: 20.8392, -156.1443
// Area: 0.05519570000000414
// Percentage: 0.01

// bottom left: 21.3245, -158.1082
// upper right: 21.5495, -157.8693
// Area: 0.05375249999999972
// Percentage: 0.01
