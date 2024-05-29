import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.patri.nutriplanner.R
import com.patri.nutriplanner.database.entities.PlanningEntity
class PlanningViewHolder(
    itemView: View,
    private val onTextChanged: (PlanningEntity) -> Unit
) : RecyclerView.ViewHolder(itemView) {

    private val tvDay: TextView = itemView.findViewById(R.id.tvDay)

    private val etBreakfast: EditText = itemView.findViewById(R.id.etBreakfast)
    private val etSnack: EditText = itemView.findViewById(R.id.etSnack)
    private val etLunch: EditText = itemView.findViewById(R.id.etLunch)
    private val etSnack2: EditText = itemView.findViewById(R.id.etSnack2)
    private val etDinner: EditText = itemView.findViewById(R.id.etDinner)

    private var currentPlanningEntity: PlanningEntity? = null

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable?) {
            currentPlanningEntity?.let {
                it.breakfast = etBreakfast.text.toString()
                it.snack = etSnack.text.toString()
                it.lunch = etLunch.text.toString()
                it.snack2 = etSnack2.text.toString()
                it.dinner = etDinner.text.toString()
                onTextChanged(it)
            }
        }
    }

    init {
        etBreakfast.addTextChangedListener(textWatcher)
        etSnack.addTextChangedListener(textWatcher)
        etLunch.addTextChangedListener(textWatcher)
        etSnack2.addTextChangedListener(textWatcher)
        etDinner.addTextChangedListener(textWatcher)
    }

    fun bind(planningEntity: PlanningEntity) {
        Log.d("PlanningViewHolder", "Binding entity: $planningEntity")
        currentPlanningEntity = planningEntity
        tvDay.text = planningEntity.label

        // Remover TextWatchers para evitar duplicaciones
        etBreakfast.removeTextChangedListener(textWatcher)
        etSnack.removeTextChangedListener(textWatcher)
        etLunch.removeTextChangedListener(textWatcher)
        etSnack2.removeTextChangedListener(textWatcher)
        etDinner.removeTextChangedListener(textWatcher)

        // Configurar EditTexts con el texto correcto
        etBreakfast.setText(planningEntity.breakfast)
        etSnack.setText(planningEntity.snack)
        etLunch.setText(planningEntity.lunch)
        etSnack2.setText(planningEntity.snack2)
        etDinner.setText(planningEntity.dinner)

        // Volver a añadir los TextWatchers
        etBreakfast.addTextChangedListener(textWatcher)
        etSnack.addTextChangedListener(textWatcher)
        etLunch.addTextChangedListener(textWatcher)
        etSnack2.addTextChangedListener(textWatcher)
        etDinner.addTextChangedListener(textWatcher)
    }
}
