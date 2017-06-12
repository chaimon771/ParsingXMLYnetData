package example.haim.parsingxml;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class YnetFragment extends Fragment implements YnetDataSource.OnYnetArrivedListener {
    RecyclerView rvYnet;

    ProgressBar progressBar;

    public YnetFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_ynet, container, false);
        YnetDataSource.getYnet(this);

        rvYnet = (RecyclerView) v.findViewById(R.id.rvYnet);
        progressBar = (ProgressBar) v.findViewById(R.id.progressBar);
        YnetDataSource.getYnet(this);

        return v;
    }

    @Override
    public void onYnetArrived(List<YnetDataSource.Ynet> data) {
        progressBar.setVisibility(View.GONE);
       if(data != null) {
           //1) rv.setLayoutManager
           rvYnet.setLayoutManager(new LinearLayoutManager(getActivity()));
           //2) rv.setAdapter
           rvYnet.setAdapter(new YnetAdapter(getActivity(), data));
       }
    }


    static class YnetAdapter extends RecyclerView.Adapter<YnetAdapter.YnetViewHolder>{
        //Properties:
        List<YnetDataSource.Ynet> data;
        LayoutInflater inflater;
        Context context;


        //Constructor:
        public YnetAdapter(Context context, List<YnetDataSource.Ynet> data) {
            this.data = data;
            this.context = context;
            this.inflater = LayoutInflater.from(context);
        }

        @Override
        public YnetViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = inflater.inflate(R.layout.ynet_item, parent, false);
            return new YnetViewHolder(v);
        }

        @Override
        public void onBindViewHolder(YnetViewHolder holder, int position) {
            final YnetDataSource.Ynet ynet = data.get(position);
            holder.tvTitle.setText(ynet.getTitle());
            holder.tvDescription.setText(ynet.getContent());

            //Picasso added from Project Structure:
            Picasso.
                    with(context).
                    load(ynet.getThumbnail()).
                    placeholder(R.drawable.thumbnail).
                    error(R.drawable.news).
                    into(holder.ivThumbnail);
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class YnetViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            //Properties
            TextView tvTitle;
            TextView tvDescription;
            ImageView ivThumbnail;

            //Constructor
            public YnetViewHolder(View itemView) {
                super(itemView);
                tvTitle = (TextView) itemView.findViewById(R.id.tvTitle);
                tvDescription = (TextView) itemView.findViewById(R.id.tvDescription);
                ivThumbnail = (ImageView) itemView.findViewById(R.id.ivThumbnail);

                itemView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                String link = data.get(position).getLink();

                if (context instanceof FragmentActivity) {
                    FragmentActivity activity = (FragmentActivity) context;

                    activity.getSupportFragmentManager().
                            beginTransaction().
                            addToBackStack("ynet_list").
                            replace(R.id.container, YnetArticleFragment.newInstance(link)).
                            commit();
                }

            }
        }
    }
}
