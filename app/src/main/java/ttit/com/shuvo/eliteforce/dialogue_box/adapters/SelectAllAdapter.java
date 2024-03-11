package ttit.com.shuvo.eliteforce.dialogue_box.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.eliteforce.R;
import ttit.com.shuvo.eliteforce.basic_model.SelectAllList;

public class SelectAllAdapter extends RecyclerView.Adapter<SelectAllAdapter.SelectView> {

    private ArrayList<SelectAllList> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;


    public SelectAllAdapter(ArrayList<SelectAllList> categoryItems, Context context, ClickedItem cli) {
        this.mCategoryItem = categoryItems;
        this.myClickedItem = cli;
        this.myContext = context;
    }

    public class SelectView extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView ftext;
        public TextView stext;
        public TextView ttext;
        public TextView fotext;

        ClickedItem mClickedItem;

        public SelectView(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            ftext = itemView.findViewById(R.id.first_item);
            stext = itemView.findViewById(R.id.second_item);
            ttext = itemView.findViewById(R.id.third_item);
            fotext = itemView.findViewById(R.id.fourth_item);


            this.mClickedItem = ci;

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            Log.i("Category Name", mCategoryItem.get(getAdapterPosition()).getFirst());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public SelectView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.all_item_list_view, parent, false);
        return new SelectView(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull SelectView holder, int position) {

        SelectAllList categoryItem = mCategoryItem.get(position);

        holder.ftext.setText(categoryItem.getFirst());
        holder.stext.setText(categoryItem.getSecond());
        holder.ttext.setText(categoryItem.getThird());
        holder.fotext.setText(categoryItem.getFourth());

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

    public void filterList(ArrayList<SelectAllList> filteredList) {
        mCategoryItem = filteredList;
        notifyDataSetChanged();
    }

}
