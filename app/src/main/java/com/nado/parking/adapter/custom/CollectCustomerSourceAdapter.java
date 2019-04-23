package com.nado.parking.adapter.custom;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;


/**
 * Created by maqing on 2017/12/8.
 * Email:2856992713@qq.com
 */

public class CollectCustomerSourceAdapter extends RecyclerView.Adapter {
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
//    private Context mContext;
//    private LayoutInflater mInflater;
//    private List<HouseBean> mHouseList;
//
//    public CollectCustomerSourceAdapter(Context context, List<HouseBean> houseList) {
//        mContext = context;
//        mHouseList = houseList;
//        mInflater = LayoutInflater.from(mContext);
//    }
//
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        RecyclerView.ViewHolder viewHolder = null;
//        switch (HouseBean.OperateType.getByValue(viewType)) {
//            case RENT_HOUSE:
//                viewHolder = new CollectCustomerSourceAdapter.RentViewHolder(mInflater.inflate(R.layout.item_rent_source, null));
//                break;
//            case BUY_HOUSE:
//                viewHolder = new CollectCustomerSourceAdapter.BuyViewHolder(mInflater.inflate(R.layout.item_buy_source, null));
//                break;
//        }
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
//        HouseBean houseBean = mHouseList.get(position);
//        RequestOptions requestOptions = new RequestOptions().circleCrop();
//        if (holder instanceof CollectCustomerSourceAdapter.RentViewHolder) {
//
//            Glide.with(mContext)
//                    .load(houseBean.getPublisherAvatar())
//                    .apply(requestOptions)
//                    .into(((RentViewHolder) holder).mPublisherAvatarIV);
//
//            ((RentViewHolder) holder).mPublisherTV.setText(houseBean.getPublisherName());
//            ((RentViewHolder) holder).mCreateTimeTV.setText(houseBean.getPublishTime());
//            ((RentViewHolder) holder).mDescribeTV.setText(houseBean.getDescription());
//            ((RentViewHolder) holder).mPriceTV.setText(houseBean.getPrice());
//            String requirement = houseBean.getRentLong() + " | " + houseBean.getRentType() + " | " + houseBean.getAddress();
//            ((RentViewHolder) holder).mRequirementTV.setText(requirement);
//        } else if (holder instanceof BuyViewHolder) {
//            Glide.with(mContext)
//                    .load(houseBean.getPublisherAvatar())
//                    .apply(requestOptions)
//                    .into(((BuyViewHolder) holder).mPublisherAvatarIV);
//
//            ((BuyViewHolder) holder).mPublisherTV.setText(houseBean.getPublisherName());
//            ((BuyViewHolder) holder).mCreateTimeTV.setText(houseBean.getPublishTime());
//            ((BuyViewHolder) holder).mTagTV.setText(houseBean.getBuyHouseTag());
//            ((BuyViewHolder) holder).mDescribeTV.setText(houseBean.getDescription());
//            ((BuyViewHolder) holder).mPriceTV.setText(houseBean.getPrice());
//            ((BuyViewHolder) holder).mAddressTV.setText(houseBean.getAddress());
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return mHouseList.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return mHouseList.get(position).getOperateType().getValue();
//    }
//
//    private class RentViewHolder extends RecyclerView.ViewHolder {
//        private ImageView mPublisherAvatarIV;
//        private TextView mPublisherTV;
//        private TextView mCreateTimeTV;
//        private TextView mDescribeTV;
//        private TextView mPriceTV;
//        private TextView mRequirementTV;
//
//        public RentViewHolder(View itemView) {
//            super(itemView);
//            mPublisherAvatarIV = itemView.findViewById(R.id.iv_item_rent_source_publisher_avatar);
//            mPublisherTV = itemView.findViewById(R.id.tv_item_rent_source_publisher);
//            mCreateTimeTV = itemView.findViewById(R.id.tv_item_rent_source_create_time);
//            mDescribeTV = itemView.findViewById(R.id.tv_item_rent_source_describe);
//            mPriceTV = itemView.findViewById(R.id.tv_item_rent_source_price);
//            mRequirementTV = itemView.findViewById(R.id.tv_item_rent_source_requirement);
//        }
//    }
//
//    private class BuyViewHolder extends RecyclerView.ViewHolder {
//        private ImageView mPublisherAvatarIV;
//        private TextView mPublisherTV;
//        private TextView mCreateTimeTV;
//        private TextView mTagTV;
//        private TextView mDescribeTV;
//        private TextView mPriceTV;
//        private TextView mAddressTV;
//
//        public BuyViewHolder(View itemView) {
//            super(itemView);
//            mPublisherAvatarIV = itemView.findViewById(R.id.iv_item_buy_source_publisher_avatar);
//            mPublisherTV = itemView.findViewById(R.id.tv_item_buy_source_publisher);
//            mCreateTimeTV = itemView.findViewById(R.id.tv_item_buy_source_create_time);
//            mTagTV = itemView.findViewById(R.id.tv_item_buy_source_tag);
//            mDescribeTV = itemView.findViewById(R.id.tv_item_buy_source_describe);
//            mPriceTV = itemView.findViewById(R.id.tv_item_buy_source_price);
//            mAddressTV = itemView.findViewById(R.id.tv_item_buy_source_address);
//        }
//    }
}
