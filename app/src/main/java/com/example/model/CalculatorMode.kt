package com.example.model

enum class CalculatorMode(val id: Int, val nameEn: String, val nameAr: String, val shortIndicator: String) {
    CALCULATE(1, "Calculate", "الحساب العلمي", "COMP"),
    COMPLEX(2, "Complex", "الأعداد المركبة", "CMPLX"),
    BASE_N(3, "Base-N", "الأنظمة العددية", "BASE"),
    MATRIX(4, "Matrix", "المصفوفات", "MAT"),
    VECTOR(5, "Vector", "المتجهات", "VCT"),
    STATISTICS(6, "Statistics", "الإحصاء والبيانات", "STAT"),
    TABLE(7, "Table & Graph", "الجدول والرسم البياني", "TABLE"),
    EQUATION(8, "Equation Solver", "حل المعادلات", "EQN"),
    UNIT_CONVERTER(9, "Unit Converter", "محول الوحدات الشامل", "CONV")
}
