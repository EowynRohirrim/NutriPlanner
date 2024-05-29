import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.patri.nutriplanner.R
import com.patri.nutriplanner.database.entities.PlanningEntity

class PlanningAdapter(
    private var dayList: List<PlanningEntity> = emptyList(),
    private val onTextChanged: (PlanningEntity) -> Unit
) : RecyclerView.Adapter<PlanningViewHolder>() {

    fun updateList(list: List<PlanningEntity>) {
        Log.d("PlanningAdapter", "Updating list: $list")
        dayList = list
        notifyDataSetChanged()
    }

    fun getList(): List<PlanningEntity> {
        return dayList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanningViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_planning, parent, false)
        return PlanningViewHolder(view, onTextChanged)
    }

    override fun onBindViewHolder(holder: PlanningViewHolder, position: Int) {
        val planningEntity = dayList[position]
        Log.d("PlanningAdapter", "Binding data for position $position: $planningEntity")
        holder.bind(planningEntity)
    }

    override fun getItemCount() = dayList.size
}

