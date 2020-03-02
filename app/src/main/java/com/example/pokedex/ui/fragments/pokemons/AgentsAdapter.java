package com.example.pokedex.ui.fragments.sms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.aimservices.telabook.R;
import com.aimservices.telabook.models.agents.Agent;
import com.aimservices.telabook.models.firebase.WasNotSeen;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.textview.MaterialTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class AgentsAdapter extends RecyclerView.Adapter<AgentsAdapter.ViewHolder> {

    private ArrayList<Agent> mAgents;
    private final AgentsFragment.OnAgentInteractionListener mListener;
    private boolean isShimmering = true;
    private ArrayList<WasNotSeen> unreadMessages = new ArrayList<>();

    AgentsAdapter(AgentsFragment.OnAgentInteractionListener listener) {
        mAgents = new ArrayList<>();
        mListener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.agent_item, parent, false);
        return new AgentsAdapter.ViewHolder(view);
    }

    @Override
    public long getItemId(int position) {
        if (mAgents.size() > 0) {
            Agent agent = mAgents.get(position);
            return agent != null ? agent.getWorkerId() : super.getItemId(position);
        }
        return super.getItemId(position);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (isShimmering) {
            holder.shimmer.startShimmer();
        } else {
            holder.shimmer.stopShimmer();
            holder.shimmer.hideShimmer();
            holder.bind(mAgents.get(position));
        }
    }

    public void setAgents(List<Agent> agents) {
        isShimmering = false;

        this.mAgents = new ArrayList<>(agents);
        refreshData();
    }

    private void refreshData() {
        for (int i = 0; i < mAgents.size(); i++) {
            mAgents.get(i).setUnreadMessagesCount(0);
        }
        for (WasNotSeen w : unreadMessages) {
            for (int i = 0; i < mAgents.size(); i++) {
                if (mAgents.get(i).getWorkerId() == w.getWorker_id()) {
                    mAgents.get(i).setUnreadMessagesCount(mAgents.get(i).getUnreadMessagesCount()+w.getCount());
                    mAgents.get(i).setLastMessageDate(w.getDate());
                }

            }
        }
        Collections.sort(mAgents);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return isShimmering ? 5 : mAgents.size();
    }

    void updateUnreadMessages(ArrayList<WasNotSeen> wasNotSeens) {
        this.unreadMessages = wasNotSeens;
        refreshData();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        final ShimmerFrameLayout shimmer;
        final CircleImageView agentsProfile;
        Agent mAgent;
        final View mView;
        final MaterialTextView agentName;
        final MaterialTextView messageCount;

        ViewHolder(View view) {
            super(view);
            mView = view;
            this.shimmer = view.findViewById(R.id.shimmer_view_container);
            this.agentsProfile = view.findViewById(R.id.agent_profile_image);

            this.agentName = view.findViewById(R.id.agent_name);
            this.messageCount = view.findViewById(R.id.message_count);
        }

        void bind(Agent agent) {
            mAgent = agent;
            agentName.setText(agent.getPersonName());

            if (agent.getUnreadMessagesCount() < 1) {
                messageCount.setVisibility(View.GONE);
            } else {
                messageCount.setVisibility(View.VISIBLE);
                messageCount.setText(String.valueOf(agent.getUnreadMessagesCount()));
            }
            mView.setOnClickListener(v -> {
                if (null != mListener) {
                    mListener.agentTouched(agent);
                }
            });

            agentName.setBackground(null);
            agentsProfile.setBackground(null);
            agentsProfile.setBorderColor(mView.getContext().getColor(R.color.colorPrimary));
            if (agent.getProfileImageUrl() != null) {
                Picasso.get().load(agent.getProfileImageUrl()).into(agentsProfile);
            }


        }
    }
}
