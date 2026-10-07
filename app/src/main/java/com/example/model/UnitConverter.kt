package com.example.model

enum class UnitCategory(val nameEn: String, val nameAr: String) {
    LENGTH("Length", "الطول"),
    AREA("Area", "المساحة"),
    VOLUME("Volume", "الحجم"),
    CURRENCY("Currency", "العملات"),
    FUEL("Fuel Consumption", "استهلاك الوقود"),
    TIME("Time", "الوقت"),
    SPEED("Speed", "السرعة"),
    ACCELERATION("Acceleration", "التسارع"),
    ANGULAR_SPEED("Angular Speed", "السرعة الزاوية"),
    FLOW_RATE("Flow Rate", "معدل التدفق"),
    FREQUENCY("Frequency", "التردد"),
    DENSITY("Density", "الكثافة"),
    DYNAMIC_VISCOSITY("Dynamic Viscosity", "اللزوجة الديناميكية"),
    KINEMATIC_VISCOSITY("Kinematic Viscosity", "اللزوجة الحركية"),
    MINERALIZATION("Mineralization", "التمعدن والتركيز"),
    DATA_DECIMAL("Data (1KB = 1000B)", "تخزين البيانات (عشري)"),
    DATA_BINARY("Data (1KiB = 1024B)", "تخزين البيانات (ثنائي)"),
    DATA_TRANSFER("Data Transfer", "سرعة نقل البيانات"),
    ENERGY("Energy", "الطاقة")
}

data class ConversionUnit(
    val id: String,
    val nameEn: String,
    val nameAr: String,
    val symbol: String,
    val factorToBase: Double // Multiply by this factor to convert to base unit
)

object UnitConverterData {
    val categories: Map<UnitCategory, List<ConversionUnit>> = mapOf(
        UnitCategory.LENGTH to listOf(
            ConversionUnit("m", "Meter", "متر", "m", 1.0),
            ConversionUnit("km", "Kilometer", "كيلومتر", "km", 1000.0),
            ConversionUnit("cm", "Centimeter", "سنتيمتر", "cm", 0.01),
            ConversionUnit("mm", "Millimeter", "ميليمتر", "mm", 0.001),
            ConversionUnit("mi", "Mile", "ميل", "mi", 1609.344),
            ConversionUnit("yd", "Yard", "ياردة", "yd", 0.9144),
            ConversionUnit("ft", "Foot", "قدم", "ft", 0.3048),
            ConversionUnit("in", "Inch", "بوصة", "in", 0.0254),
            ConversionUnit("nmi", "Nautical Mile", "ميل بحري", "NM", 1852.0)
        ),
        UnitCategory.AREA to listOf(
            ConversionUnit("sq_m", "Square Meter", "متر مربع", "m²", 1.0),
            ConversionUnit("sq_km", "Square Kilometer", "كيلومتر مربع", "km²", 1e6),
            ConversionUnit("ha", "Hectare", "هكتار", "ha", 10000.0),
            ConversionUnit("acre", "Acre", "فدان / أكر", "ac", 4046.856),
            ConversionUnit("sq_mi", "Square Mile", "ميل مربع", "mi²", 2.58999e6),
            ConversionUnit("sq_yd", "Square Yard", "ياردة مربعة", "yd²", 0.836127),
            ConversionUnit("sq_ft", "Square Foot", "قدم مربع", "ft²", 0.092903),
            ConversionUnit("sq_in", "Square Inch", "بوصة مربعة", "in²", 0.00064516)
        ),
        UnitCategory.VOLUME to listOf(
            ConversionUnit("l", "Liter", "لتر", "L", 1.0),
            ConversionUnit("ml", "Milliliter", "ميليلتر", "mL", 0.001),
            ConversionUnit("cu_m", "Cubic Meter", "متر مكعب", "m³", 1000.0),
            ConversionUnit("bbl", "Barrel (Oil)", "برميل نفط", "bbl", 158.9873),
            ConversionUnit("gal_us", "Gallon (US)", "غالون أمريكي", "gal", 3.78541),
            ConversionUnit("fl_oz_us", "Fluid Ounce (US)", "أونصة سائلة", "fl oz", 0.0295735),
            ConversionUnit("cu_ft", "Cubic Foot", "قدم مكعب", "ft³", 28.3168),
            ConversionUnit("pt_us", "Pint (US)", "باينت", "pt", 0.473176)
        ),
        UnitCategory.CURRENCY to listOf(
            ConversionUnit("usd", "US Dollar", "دولار أمريكي", "USD", 1.0),
            ConversionUnit("eur", "Euro", "يورو", "EUR", 1.08),
            ConversionUnit("gbp", "British Pound", "جنيه إسترليني", "GBP", 1.28),
            ConversionUnit("chf", "Swiss Franc", "فرنك سويسري", "CHF", 1.13),
            ConversionUnit("sar", "Saudi Riyal", "ريال سعودي", "SAR", 0.2666),
            ConversionUnit("aed", "UAE Dirham", "درهم إماراتي", "AED", 0.2723),
            ConversionUnit("kwd", "Kuwaiti Dinar", "دينار كويتي", "KWD", 3.25),
            ConversionUnit("egp", "Egyptian Pound", "جنيه مصري", "EGP", 0.0205),
            ConversionUnit("jpy", "Japanese Yen", "ين ياباني", "JPY", 0.0066)
        ),
        UnitCategory.FUEL to listOf(
            ConversionUnit("l_100km", "Liters per 100 km", "لتر لكل 100 كم", "L/100km", 1.0),
            ConversionUnit("mpg_us", "Miles per Gallon (US)", "ميل لكل غالون أمريكي", "MPG US", 1.0),
            ConversionUnit("mpg_uk", "Miles per Gallon (UK)", "ميل لكل غالون بريطاني", "MPG UK", 1.0),
            ConversionUnit("km_l", "Kilometers per Liter", "كيلومتر لكل لتر", "km/L", 1.0)
        ),
        UnitCategory.TIME to listOf(
            ConversionUnit("s", "Second", "ثانية", "s", 1.0),
            ConversionUnit("min", "Minute", "دقيقة", "min", 60.0),
            ConversionUnit("h", "Hour", "ساعة", "h", 3600.0),
            ConversionUnit("d", "Day", "يوم", "d", 86400.0),
            ConversionUnit("wk", "Week", "أسبوع", "wk", 604800.0),
            ConversionUnit("mo", "Month (Avg)", "شهر", "mo", 2.628e6),
            ConversionUnit("yr", "Year", "سنة", "yr", 3.1536e7),
            ConversionUnit("ms", "Millisecond", "ميلي ثانية", "ms", 0.001)
        ),
        UnitCategory.SPEED to listOf(
            ConversionUnit("m_s", "Meter per Second", "متر لكل ثانية", "m/s", 1.0),
            ConversionUnit("km_h", "Kilometer per Hour", "كيلومتر لكل ساعة", "km/h", 0.277778),
            ConversionUnit("mph", "Mile per Hour", "ميل لكل ساعة", "mph", 0.44704),
            ConversionUnit("knot", "Knot", "عقدة بحرية", "kn", 0.514444),
            ConversionUnit("ft_s", "Foot per Second", "قدم لكل ثانية", "ft/s", 0.3048)
        ),
        UnitCategory.ACCELERATION to listOf(
            ConversionUnit("m_s2", "Meter per Second²", "متر لكل ثانية²", "m/s²", 1.0),
            ConversionUnit("g0", "Standard Gravity", "جاذبية قياسية", "g", 9.80665),
            ConversionUnit("ft_s2", "Foot per Second²", "قدم لكل ثانية²", "ft/s²", 0.3048),
            ConversionUnit("m_h_s", "Meter per Hour per Sec", "متر لكل ساعة لكل ثانية", "m/(h·s)", 1.0 / 3600.0)
        ),
        UnitCategory.ANGULAR_SPEED to listOf(
            ConversionUnit("rad_s", "Radian per Second", "راديان لكل ثانية", "rad/s", 1.0),
            ConversionUnit("deg_s", "Degree per Second", "درجة لكل ثانية", "°/s", Math.PI / 180.0),
            ConversionUnit("rpm", "Revolution per Minute", "دورة لكل دقيقة", "RPM", 2.0 * Math.PI / 60.0),
            ConversionUnit("rph", "Revolution per Hour", "دورة لكل ساعة", "RPH", 2.0 * Math.PI / 3600.0),
            ConversionUnit("deg_h", "Degree per Hour", "درجة لكل ساعة", "°/h", Math.PI / (180.0 * 3600.0))
        ),
        UnitCategory.FLOW_RATE to listOf(
            ConversionUnit("l_min", "Liter per Minute", "لتر لكل دقيقة", "L/min", 1.0),
            ConversionUnit("l_s", "Liter per Second", "لتر لكل ثانية", "L/s", 60.0),
            ConversionUnit("gpm_us", "Gallon per Minute (US)", "غالون لكل دقيقة", "GPM", 3.78541),
            ConversionUnit("cu_m_h", "Cubic Meter per Hour", "متر مكعب لكل ساعة", "m³/h", 1000.0 / 60.0)
        ),
        UnitCategory.FREQUENCY to listOf(
            ConversionUnit("hz", "Hertz", "هيرتز", "Hz", 1.0),
            ConversionUnit("khz", "KiloHertz", "كيلوهيرتز", "kHz", 1e3),
            ConversionUnit("mhz", "MegaHertz", "ميغاهيرتز", "MHz", 1e6),
            ConversionUnit("ghz", "GigaHertz", "غيغاهيرتز", "GHz", 1e9),
            ConversionUnit("rpm", "Cycles per Minute", "دورة بالدقيقة", "cpm", 1.0 / 60.0)
        ),
        UnitCategory.DENSITY to listOf(
            ConversionUnit("kg_m3", "Kilogram per m³", "كيلوغرام لكل متر مكعب", "kg/m³", 1.0),
            ConversionUnit("kg_l", "Kilogram per Liter", "كيلوغرام لكل لتر", "kg/L", 1000.0),
            ConversionUnit("g_cm3", "Gram per cm³", "غرام لكل سنتيمتر مكعب", "g/cm³", 1000.0),
            ConversionUnit("oz_gal_us", "Ounce per Gallon (US)", "أونصة لكل غالون", "oz/gal", 7.48915)
        ),
        UnitCategory.DYNAMIC_VISCOSITY to listOf(
            ConversionUnit("pa_s", "Pascal Second", "باسكال ثانية", "Pa·s", 1.0),
            ConversionUnit("p", "Poise", "بواز", "P", 0.1),
            ConversionUnit("cp", "Centipoise", "سنتي بواز", "cP", 0.001)
        ),
        UnitCategory.KINEMATIC_VISCOSITY to listOf(
            ConversionUnit("m2_s", "Square Meter per Sec", "متر مربع لكل ثانية", "m²/s", 1.0),
            ConversionUnit("st", "Stokes", "ستوكس", "St", 1e-4),
            ConversionUnit("cst", "Centistokes", "سنتي ستوكس", "cSt", 1e-6)
        ),
        UnitCategory.MINERALIZATION to listOf(
            ConversionUnit("ppm", "Parts per Million", "جزء من المليون", "PPM", 1.0),
            ConversionUnit("ppt", "Parts per Thousand", "جزء من الألف", "PPT", 1000.0),
            ConversionUnit("g_l", "Grams per Liter", "غرام لكل لتر", "g/L", 1000.0),
            ConversionUnit("mg_l", "Milligrams per Liter", "ميليغرام لكل لتر", "mg/L", 1.0)
        ),
        UnitCategory.DATA_DECIMAL to listOf(
            ConversionUnit("b", "Bit", "بت", "bit", 0.125),
            ConversionUnit("byte", "Byte", "بايت", "B", 1.0),
            ConversionUnit("kb", "Kilobyte (10³)", "كيلوبايت", "KB", 1000.0),
            ConversionUnit("mb", "Megabyte (10⁶)", "ميغابايت", "MB", 1e6),
            ConversionUnit("gb", "Gigabyte (10⁹)", "غيغابايت", "GB", 1e9),
            ConversionUnit("tb", "Terabyte (10¹²)", "تيرابايت", "TB", 1e12)
        ),
        UnitCategory.DATA_BINARY to listOf(
            ConversionUnit("bit_bin", "Bit", "بت", "bit", 0.125),
            ConversionUnit("byte_bin", "Byte", "بايت", "B", 1.0),
            ConversionUnit("kib", "Kibibyte (2¹⁰)", "كيبيبايت", "KiB", 1024.0),
            ConversionUnit("mib", "Mebibyte (2²⁰)", "ميبيبايت", "MiB", 1048576.0),
            ConversionUnit("gib", "Gibibyte (2³⁰)", "غيبيبايت", "GiB", 1073741824.0),
            ConversionUnit("tib", "Tebibyte (2⁴⁰)", "تيبيبايت", "TiB", 1099511627776.0)
        ),
        UnitCategory.DATA_TRANSFER to listOf(
            ConversionUnit("bps", "Bit per Second", "بت لكل ثانية", "bps", 1.0),
            ConversionUnit("kbps", "Kilobit per Second", "كيلوبت لكل ثانية", "kbps", 1e3),
            ConversionUnit("mbps", "Megabit per Second", "ميغابت لكل ثانية", "Mbps", 1e6),
            ConversionUnit("gbps", "Gigabit per Second", "غيغابت لكل ثانية", "Gbps", 1e9),
            ConversionUnit("kb_s", "Kilobyte per Second", "كيلوبايت لكل ثانية", "KB/s", 8e3),
            ConversionUnit("mb_s", "Megabyte per Second", "ميغابايت لكل ثانية", "MB/s", 8e6)
        ),
        UnitCategory.ENERGY to listOf(
            ConversionUnit("j", "Joule", "جول", "J", 1.0),
            ConversionUnit("kj", "KiloJoule", "كيلوجول", "kJ", 1000.0),
            ConversionUnit("cal", "Calorie", "سعر حراري", "cal", 4.184),
            ConversionUnit("kcal", "KiloCalorie", "كيلوسعر حراري", "kcal", 4184.0),
            ConversionUnit("wh", "Watt-Hour", "واط ساعة", "Wh", 3600.0),
            ConversionUnit("kwh", "KiloWatt-Hour", "كيلوواط ساعة", "kWh", 3.6e6),
            ConversionUnit("ev", "Electron-Volt", "إلكترون فولت", "eV", 1.602176634e-19),
            ConversionUnit("btu", "BTU", "وحدة حرارية بريطانية", "BTU", 1055.06)
        )
    )

    fun convert(value: Double, from: ConversionUnit, to: ConversionUnit, category: UnitCategory): Double {
        if (from.id == to.id) return value

        // Special handling for Fuel Consumption inverse calculations
        if (category == UnitCategory.FUEL) {
            val kmPerLiter = when (from.id) {
                "l_100km" -> if (value <= 0) 0.0 else 100.0 / value
                "mpg_us" -> value * 0.425144
                "mpg_uk" -> value * 0.354006
                "km_l" -> value
                else -> value
            }
            return when (to.id) {
                "l_100km" -> if (kmPerLiter <= 0) 0.0 else 100.0 / kmPerLiter
                "mpg_us" -> kmPerLiter / 0.425144
                "mpg_uk" -> kmPerLiter / 0.354006
                "km_l" -> kmPerLiter
                else -> kmPerLiter
            }
        }

        val baseValue = value * from.factorToBase
        return baseValue / to.factorToBase
    }
}
