package com.example.model

data class ScientificConstant(
    val symbol: String,
    val nameEn: String,
    val nameAr: String,
    val value: Double,
    val unit: String,
    val category: String
)

object ScientificConstantsData {
    val constants: List<ScientificConstant> = listOf(
        // Mathematical
        ScientificConstant("π", "Pi", "باي (النسبة التقريبية)", Math.PI, "", "Mathematical"),
        ScientificConstant("e", "Euler's Number", "العدد النيبيري e", Math.E, "", "Mathematical"),
        ScientificConstant("φ", "Golden Ratio", "النسبة الذهبية", 1.618033988749895, "", "Mathematical"),
        ScientificConstant("√2", "Square Root of 2", "جذر 2", Math.sqrt(2.0), "", "Mathematical"),
        ScientificConstant("ln(2)", "Natural Log of 2", "لوغاريتم 2 الطبيعي", Math.log(2.0), "", "Mathematical"),

        // Universal
        ScientificConstant("c", "Speed of Light in Vacuum", "سرعة الضوء في الفراغ", 299792458.0, "m/s", "Universal"),
        ScientificConstant("G", "Newtonian Gravitational Constant", "ثابت الجاذبية العام", 6.67430e-11, "m³/(kg·s²)", "Universal"),
        ScientificConstant("h", "Planck Constant", "ثابت بلانك", 6.62607015e-34, "J·s", "Universal"),
        ScientificConstant("ħ", "Reduced Planck Constant", "ثابت بلانك المختزل", 1.054571817e-34, "J·s", "Universal"),
        ScientificConstant("μ₀", "Vacuum Magnetic Permeability", "النفاذية المغناطيسية للفراغ", 1.25663706212e-6, "N/A²", "Universal"),
        ScientificConstant("ε₀", "Vacuum Electric Permittivity", "السماحية الكهربائية للفراغ", 8.8541878128e-12, "F/m", "Universal"),
        ScientificConstant("Z₀", "Characteristic Impedance of Vacuum", "مقاومة الفراغ المميزة", 376.730313668, "Ω", "Universal"),

        // Electromagnetic
        ScientificConstant("e", "Elementary Charge", "شحنة الإلكترون الأولية", 1.602176634e-19, "C", "Electromagnetic"),
        ScientificConstant("Φ₀", "Magnetic Flux Quantum", "كم الفيض المغناطيسي", 2.067833848e-15, "Wb", "Electromagnetic"),
        ScientificConstant("G₀", "Conductance Quantum", "كم التوصيلية", 7.748091729e-5, "S", "Electromagnetic"),
        ScientificConstant("K_J", "Josephson Constant", "ثابت جوزيفسون", 483597.8484e9, "Hz/V", "Electromagnetic"),
        ScientificConstant("R_K", "Von Klitzing Constant", "ثابت فون كليتزينغ", 25812.80745, "Ω", "Electromagnetic"),

        // Atomic & Nuclear
        ScientificConstant("m_e", "Electron Mass", "كتلة الإلكترون", 9.1093837015e-31, "kg", "Atomic & Nuclear"),
        ScientificConstant("m_p", "Proton Mass", "كتلة البروتون", 1.67262192369e-27, "kg", "Atomic & Nuclear"),
        ScientificConstant("m_n", "Neutron Mass", "كتلة النيوترون", 1.67492749804e-27, "kg", "Atomic & Nuclear"),
        ScientificConstant("m_u", "Atomic Mass Constant", "وحدة الكتل الذرية", 1.66053906660e-27, "kg", "Atomic & Nuclear"),
        ScientificConstant("a₀", "Bohr Radius", "نصف قطر بور", 5.29177210903e-11, "m", "Atomic & Nuclear"),
        ScientificConstant("α", "Fine-Structure Constant", "ثابت البناء الدقيق", 7.2973525693e-3, "", "Atomic & Nuclear"),
        ScientificConstant("R_∞", "Rydberg Constant", "ثابت ريدبرغ", 10973731.568160, "m⁻¹", "Atomic & Nuclear"),
        ScientificConstant("μ_B", "Bohr Magneton", "مغناطون بور", 9.2740100783e-24, "J/T", "Atomic & Nuclear"),
        ScientificConstant("μ_N", "Nuclear Magneton", "مغناطون نووي", 5.0507837461e-27, "J/T", "Atomic & Nuclear"),

        // Physico-Chemical
        ScientificConstant("N_A", "Avogadro Constant", "عدد أفوغادرو", 6.02214076e23, "mol⁻¹", "Physico-Chemical"),
        ScientificConstant("k_B", "Boltzmann Constant", "ثابت بولتزمان", 1.380649e-23, "J/K", "Physico-Chemical"),
        ScientificConstant("R", "Molar Gas Constant", "ثابت الغازات العام", 8.314462618, "J/(mol·K)", "Physico-Chemical"),
        ScientificConstant("F", "Faraday Constant", "ثابت فاراداي", 96485.33212, "C/mol", "Physico-Chemical"),
        ScientificConstant("σ", "Stefan-Boltzmann Constant", "ثابت ستيفان-بولتزمان", 5.670374419e-8, "W/(m²·K⁴)", "Physico-Chemical"),
        ScientificConstant("V_m", "Molar Volume of Ideal Gas", "الحجم المولي للغاز المثالي", 0.02271095464, "m³/mol", "Physico-Chemical"),
        ScientificConstant("g", "Standard Acceleration of Gravity", "تسارع الجاذبية الأرضية القياسي", 9.80665, "m/s²", "Physico-Chemical")
    )
}
