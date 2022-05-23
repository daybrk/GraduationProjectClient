package com.example.graduationprojectclient.rv;

import android.graphics.Canvas;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.graduationprojectclient.service.CommunicationWithServerService;
import com.example.graduationprojectclient.R;
import com.example.graduationprojectclient.interfaces.ItemTouchHelperAdapter;
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
                .addSwipeLeftBackgroundColor(ContextCompat.getColor(view.getContext(), R.color.colorAccent))
                .addSwipeLeftActionIcon(R.drawable.ic_baseline_delete_sweep_24)
                .addSwipeRightBackgroundColor(R.color.colorAccent)
                .addSwipeRightActionIcon(R.drawable.ic_baseline_assignment_turned_in_24)
                .create()
                .decorate();
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    static int position;

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        position = viewHolder.getAdapterPosition();

        switch (direction) {
            case ItemTouchHelper.LEFT:
                adapter.onItemDismiss(position);
                Snackbar left = Snackbar.make(view, "Предложение № " + position, Snackbar.LENGTH_LONG);
                left.setAction("Отменить удаление", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.onItemReturned(position);
                    }
                }).show();
                left.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                            Call<ResponseBody> call = CommunicationWithServerService.getApiService()
                                    .deleteSuggestion(adapter.findSuggestionByPosition(position).getSuggestionId(), CommunicationWithServerService.getEMAIL());
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
                        }
                        super.onDismissed(transientBottomBar, event);
                    }
                });
            break;

            case ItemTouchHelper.RIGHT:
                adapter.onItemDismiss(position);
                Snackbar right = Snackbar.make(view, "Предложение № " + position, Snackbar.LENGTH_LONG);
                right.setAction("Отменить одобрение", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        adapter.onItemReturned(position);
                    }
                }).show();
                right.addCallback(new Snackbar.Callback() {
                    @Override
                    public void onDismissed(Snackbar transientBottomBar, int event) {
                        if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                            Call<ResponseBody> call = CommunicationWithServerService.getApiService()
                                    .confirmSuggestion(adapter.findSuggestionByPosition(position).getSuggestionId(), CommunicationWithServerService.getEMAIL());
                            call.enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    t.printStackTrace();
                                }
                            });
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
