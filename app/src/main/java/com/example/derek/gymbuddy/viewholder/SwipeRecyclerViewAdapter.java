//package com.example.derek.gymbuddy.viewholder;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.daimajia.swipe.SwipeLayout;
//import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;
//import com.example.derek.gymbuddy.R;
//import com.example.derek.gymbuddy.models.Routine;
//
//import java.util.ArrayList;
//
//public class SwipeRecyclerViewAdapter extends RecyclerSwipeAdapter<SwipeRecyclerViewAdapter.SimpleViewHolder> {
//
//    private Context mContext;
//    private ArrayList<Routine> routineList;
//    public SwipeRecyclerViewAdapter(Context context, ArrayList<Routine> objects) {
//        this.mContext = context;
//        this.routineList = objects;
//    }
//
//
//    @Override
//    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_layout, parent, false);
//        return new SimpleViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final SimpleViewHolder viewHolder, final int position) {
//        final Routine item = routineList.get(position);
//
//        viewHolder.tvName.setText(item.getRoutine());
//        viewHolder.tvWeight.setText(item.getWeight());
//        viewHolder.tvSets.setText(item.getSets());
//        viewHolder.tvReps.setText(item.getReps());
//
//        viewHolder.swipeLayout.setShowMode(SwipeLayout.ShowMode.PullOut);
//
//
//        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Left, viewHolder.swipeLayout.findViewById(R.id.bottom_wrapper1));
//
//
//        viewHolder.swipeLayout.addDrag(SwipeLayout.DragEdge.Right, viewHolder.swipeLayout.findViewById(R.id.bottom_wraper));
//
//
//
//        viewHolder.swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
//            @Override
//            public void onStartOpen(SwipeLayout layout) {
//
//            }
//
//            @Override
//            public void onOpen(SwipeLayout layout) {
//
//            }
//
//            @Override
//            public void onStartClose(SwipeLayout layout) {
//
//            }
//
//            @Override
//            public void onClose(SwipeLayout layout) {
//
//            }
//
//            @Override
//            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
//
//            }
//
//            @Override
//            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
//
//            }
//        });
//
//        viewHolder.swipeLayout.getSurfaceView().setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext, " Click : " + item.getRoutine() + " \n" + item.getWeight(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        viewHolder.btnLocation.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "Clicked on Information " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        viewHolder.Share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Toast.makeText(view.getContext(), "Clicked on Share " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        viewHolder.Edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Toast.makeText(view.getContext(), "Clicked on Edit  " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        viewHolder.Delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mItemManger.removeShownLayouts(viewHolder.swipeLayout);
//                routineList.remove(position);
//                notifyItemRemoved(position);
//                notifyItemRangeChanged(position, routineList.size());
//                mItemManger.closeAllItems();
//                Toast.makeText(v.getContext(), "Deleted " + viewHolder.tvName.getText().toString(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        mItemManger.bindView(viewHolder.itemView, position);
//    }
//
//    @Override
//    public int getItemCount() {
//        return routineList.size();
//    }
//
//    @Override
//    public int getSwipeLayoutResourceId(int position) {
//        return R.id.swipe;
//    }
//
//    public static class SimpleViewHolder extends RecyclerView.ViewHolder{
//        public SwipeLayout swipeLayout;
//        public TextView Delete;
//        public TextView Edit;
//        public TextView Share;
//        public TextView tvName, tvWeight, tvReps, tvSets;
//        public ImageButton btnLocation;
//        public SimpleViewHolder(View itemView) {
//            super(itemView);
//            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe);
//            tvName = (TextView) itemView.findViewById(R.id.routine);
//            tvWeight = (TextView) itemView.findViewById(R.id.weight);
//            tvReps = (TextView) itemView.findViewById(R.id.reps);
//            tvSets = (TextView) itemView.findViewById(R.id.sets);
//            Delete = (TextView) itemView.findViewById(R.id.Delete);
//            Edit = (TextView) itemView.findViewById(R.id.Edit);
//            Share = (TextView) itemView.findViewById(R.id.Share);
//            btnLocation = (ImageButton) itemView.findViewById(R.id.btnLocation);
//        }
//    }
//}
