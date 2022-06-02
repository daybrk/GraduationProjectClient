package com.example.graduationprojectclient.rv;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.activity.LogInActivity;
import com.example.graduationprojectclient.interfaces.ItemTouchHelperAdapter;
import com.example.graduationprojectclient.service.CommunicationWithServerService;
import com.google.android.material.snackbar.Snackbar;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimpleItemTouchHelperCallback extends ItemTouchHelper.SimpleCallback {

    private final ItemTouchHelperAdapter adapter;
    private final View view;


    public SimpleItemTouchHelperCallback(int dragDirs, int swipeDirs, ItemTouchHelperAdapter adapter, View view) {
        super(dragDirs, swipeDirs);
        this.adapter = adapter;
        this.view = view;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {

        new RecyclerViewSwipeDecorator.Builder(view.getContext(), c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.delete))
                .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_sweep_24)
                .addSwipeRightBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.accept))
                .addSwipeRightActionIcon(R.drawable.ic_baseline_assignment_turned_in_24)
                .create()
                .decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    Snackbar snackbar;
    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    adapter.onItemDismiss(viewHolder.getAdapterPosition());
                    snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Отменить отказ", view -> adapter.onItemReturned(0)).show();
                    snackbar.addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                                Call<ResponseBody> call = CommunicationWithServerService.getApiService()
                                        .canceledSuggestion(adapter.findSuggestionByPosition(viewHolder.getAdapterPosition()).getSuggestionId(),
                                                LogInActivity.getInstance().getDb().loginDao().getLogin().getEmail());
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                        adapter.onItemReturned(2);
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                        t.printStackTrace();
                                    }
                                });
                            } else if (event == Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE) {
                                adapter.onItemReturned(1);
                            }
                            super.onDismissed(transientBottomBar, event);
                        }
                    });
                    break;

                case ItemTouchHelper.RIGHT:
                    adapter.onItemDismiss(viewHolder.getAdapterPosition());
                    snackbar = Snackbar.make(view, "", Snackbar.LENGTH_LONG);
                    snackbar.setAction("Отменить одобрение", view -> adapter.onItemReturned(0)).show();
                    snackbar.addCallback(new Snackbar.Callback() {
                        @Override
                        public void onDismissed(Snackbar transientBottomBar, int event) {
                            if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                                Call<ResponseBody> call = CommunicationWithServerService.getApiService()
                                        .confirmSuggestion(adapter.findSuggestionByPosition(viewHolder.getAdapterPosition()).getSuggestionId(),
                                                LogInActivity.getInstance().getDb().loginDao().getLogin().getEmail());
                                call.enqueue(new Callback<ResponseBody>() {
                                    @Override
                                    public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                                        adapter.onItemReturned(2);
                                    }

                                    @Override
                                    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                                        t.printStackTrace();
                                    }
                                });
                            } else if (event == Snackbar.Callback.DISMISS_EVENT_CONSECUTIVE) {
                                adapter.onItemReturned(1);
                            }
                            super.onDismissed(transientBottomBar, event);
                        }
                    });
                    break;
            }

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }
}
