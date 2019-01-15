package jp.co.nri.route.adapter;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.nri.route.R;
import jp.co.nri.route.base.BaseApplication;
import jp.co.nri.route.bean.Event;

/**
 * EventList adapter
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener {

    private ItemClickListener listener;
    private List<Event> list;
    private long sysTime;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat ymd = new SimpleDateFormat("yyyy/MM/dd");

    public MyAdapter(List<Event> list) {
        this.list = list;
    }

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    public void setSysTime(long sysTime){
        this.sysTime = sysTime;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event, parent, false);
        MyAdapter.ViewHolder viewHolder = new MyAdapter.ViewHolder(view);
        view.setOnClickListener(this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        Event event = list.get(position);
        holder.tvTitle.setText(event.getName());
        holder.tvSubTitle.setText(event.getDetail());
        holder.tvStatus.setVisibility(View.GONE);
        holder.rlItem.setTag(position);
        try {
            long sTime = sdf.parse(event.getStartDate()+" "+event.getStartTime()).getTime();
            long eTime = sdf.parse(event.getEndDate()+" "+event.getEndTime()).getTime();
            String sysDate = ymd.format(new Date(sysTime));
            if(sysTime >= sTime && sysTime <= eTime){
                holder.tvStatus.setText("開催中!");
                holder.tvStatus.setVisibility(View.VISIBLE);
            }else if(sysDate.compareTo(event.getStartDate()) >= 0 && sysDate.compareTo(event.getEndDate()) <= 0){
                holder.tvStatus.setText("本日開催!");
                holder.tvStatus.setVisibility(View.VISIBLE);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        int p = (int) v.getTag();
        Event event = list.get(p);
        if(listener == null || event.getUserid() == null){
            return;
        }
        if(event.getUserid().equals(BaseApplication.getApplication().userId)){
            listener.onItemClick(event, true);
        }else{
            listener.onItemClick(event, false);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tvTitle) TextView tvTitle;
        @BindView(R.id.tvSubTitle) TextView tvSubTitle;
        @BindView(R.id.tvStatus) TextView tvStatus;
        @BindView(R.id.rlItem) View rlItem;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public interface ItemClickListener{
        void onItemClick(Event event, boolean isUser);
    }
}