package ma.chablou.adria.adapters;


/**
 * Created by Faical chablou on 21/10/2018.
 */

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ma.chablou.adria.R;
import ma.chablou.adria.model.AlbumItem;

import java.util.Collections;
import java.util.List;



//Adapter pour l'affichage des albums les albums

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private List<AlbumItem> mData = Collections.emptyList();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // les donnnes sont passes avec le constructeur
    public AlbumAdapter(Context context, List<AlbumItem> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflate le row
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_album, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds des donnnees a  textview dans chaque row

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String name = mData.get(position).getNameAlbum();
        holder.myTextView.setText(name);
    }

    // Nombre total des rows
    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView myTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            myTextView = (TextView) itemView.findViewById(R.id.nomAlbum);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // lorsqu'on click dans une position
    public String getItem(int id) {
        return mData.get(id).getIdAlbum();
    }


    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}