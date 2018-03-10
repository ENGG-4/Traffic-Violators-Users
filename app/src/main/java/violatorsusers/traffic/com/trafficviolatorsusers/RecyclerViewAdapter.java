package violatorsusers.traffic.com.trafficviolatorsusers;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private List<ReportListItem> reportList;

    public RecyclerViewAdapter(Context context, List<ReportListItem> reportList) {
        this.context = context;
        this.reportList = reportList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ReportListItem reportItem = reportList.get(position);
        holder.image.setBackgroundResource(reportItem.getPhoto());
        holder.vehicle.setText(reportItem.getVehicleNo());
        holder.reason.setText(reportItem.getReason());
        holder.date.setText(reportItem.getReportDate());
        holder.time.setText(reportItem.getReportTime());
        holder.fine.setText(reportItem.getFine());
    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    public void setFilter(List<ReportListItem> list) {
        reportList = new ArrayList<>();
        reportList.addAll(list);
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView vehicle, reason, date,time,fine;
        public ImageView image;

        public MyViewHolder(View view) {
            super(view);
            image = (ImageView) view.findViewById(R.id.iv_item_icon);
            vehicle = (TextView) view.findViewById(R.id.txt_item_vehicleno);
            reason = (TextView) view.findViewById(R.id.txt_item_reason);
            date = (TextView) view.findViewById(R.id.txt_item_date);
            time = (TextView) view.findViewById(R.id.txt_item_time);
            fine = (TextView) view.findViewById(R.id.txt_item_fine);
        }
    }
}