package com.example.ui.dialogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ui.theme.KeyShiftGold
import com.example.ui.theme.PrimaryCyan

data class HelpSection(
    val titleEn: String,
    val titleAr: String,
    val contentEn: String,
    val contentAr: String
)

@Composable
fun HelpDialog(
    isArabic: Boolean,
    onDismiss: () -> Unit
) {
    val sections = listOf(
        HelpSection(
            "Natural Textbook Display & S⇔D",
            "العرض الطبيعي والتبديل [S⇔D]",
            "Displays fractions, radicals, powers, integrals and derivatives just like textbooks. Press S<=>D to toggle between Exact Fraction, Mixed Fraction (a b/c), and Decimal approximation.",
            "تعرض الكسور والجذور والتفاضل والتكامل كما في كتب الرياضيات. استخدم زر S⇔D للتبديل الفوري بين الكسر الدقيق والكسر المختلط والتقريب العشري."
        ),
        HelpSection(
            "Calculus (d/dx & ∫dx)",
            "التفاضل والتكامل العددي (d/dx & ∫dx)",
            "Compute numerical derivative at a point: diff(f(x), x0). Compute numerical definite integral: integrate(f(x), a, b).",
            "حساب المشتقة الأولى عند نقطة: diff(f(x), x0). وحساب التكامل المحدد بدقة عددية: integrate(f(x), a, b)."
        ),
        HelpSection(
            "Equation Solver (EQN)",
            "حل المعادلات (EQN)",
            "Solves simultaneous linear equations with 2 and 3 unknowns, and polynomials of degree 2 (with vertex/min/max), degree 3, and degree 4.",
            "حل منظومة المعادلات الخطية بمجهولين وثلاثة مجاهيل، وكثيرات الحدود من الدرجة الثانية (مع رأس المنحنى)، والدرجة الثالثة والرابعة."
        ),
        HelpSection(
            "Matrices & Vectors",
            "المصفوفات والمتجهات",
            "Supports MatA, MatB, MatC (up to 4x4) with Det, Inverse, Transpose, Powers, and Vector dot product, cross product, norm, and angle.",
            "إجراء عمليات المصفوفات حتى 4×4: المحدد، المعكوس، المدور، الضرب، والمتجهات: الضرب القياسي والاتجاهي، المعيار والزاوية."
        ),
        HelpSection(
            "Complex Numbers (CMPLX)",
            "الأعداد المركبة (CMPLX)",
            "Full arithmetic with imaginary unit i, rectangular (a + bi) and polar (r ∠ θ) representations, modulus, and argument.",
            "إجراء العمليات بالوحدة التخيلية i والتحويل التلقائي بين الصيغة الجبرية (a+bi) والقطبية (r ∠ θ)، وحساب المقياس والسعة."
        ),
        HelpSection(
            "Unit Converter (CONV)",
            "محول الوحدات الشامل (CONV)",
            "Includes Length, Area, Volume, Currency, Fuel Consumption, Time, Motion (Speed, Accel, Angular, Flow, Freq), Chemistry (Density, Viscosity, Mineralization), Computing (1000B vs 1024B, Transfer), Energy.",
            "يشمل تحويلات الطول، المساحة، الحجم، العملات، استهلاك الوقود، الوقت، الحركة، الكيمياء، الحوسبة والتخزين (عشري وثنائي)، والطاقة."
        ),
        HelpSection(
            "Scientific Constants (CONST)",
            "الثوابت العلمية (CONST)",
            "Access standard physical and mathematical constants: c, G, h, ħ, e, N_A, k_B, R, m_e, m_p, g, and insert them directly.",
            "الوصول للثوابت الفيزيائية والرياضية العالمية مثل سرعة الضوء c، ثابت الجاذبية G، ثابت بلانك h، عدد أفوغادرو N_A، وإدراجها مباشرة."
        ),
        HelpSection(
            "Memory & Variables",
            "الذاكرة والمتغيرات",
            "Store and recall variables A, B, C, D, E, F, X, Y, M, Ans, and PreAns using STO and RCL.",
            "تخزين واستدعاء المتغيرات A, B, C, D, E, F, X, Y, M بالإضافة لآخر إجابة Ans والإجابة السابقة PreAns."
        )
    )

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.8f)
                .padding(12.dp)
                .testTag("help_dialog"),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFF1E242C),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF37474F))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.HelpOutline, contentDescription = "Help", tint = KeyShiftGold)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (isArabic) "دليل المساعدة والوظائف (HELP)" else "CALCULATOR HELP GUIDE",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    IconButton(onClick = onDismiss, modifier = Modifier.testTag("help_close_btn")) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(sections) { sec ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF141A22)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = if (isArabic) sec.titleAr else sec.titleEn,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = PrimaryCyan
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = if (isArabic) sec.contentAr else sec.contentEn,
                                    fontSize = 12.sp,
                                    color = Color(0xFFCFD8DC),
                                    lineHeight = 17.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
