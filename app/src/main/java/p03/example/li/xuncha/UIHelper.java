package p03.example.li.xuncha;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by li on 2018/9/30.
 */
public class UIHelper {
    private static Dialog mLoadingDialog;
    /**
     * 显示加载对话框
     *
     * @param context    上下文
     * @param msg        对话框显示内容
     */
    public static void showDialogForLoading(Context context, String msg) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_loading_dialog, null);
        TextView loadingText = (TextView) view.findViewById(R.id.id_tv_loading_dialog_text);
        AVLoadingIndicatorView avLoadingIndicatorView = (AVLoadingIndicatorView) view.findViewById(R.id.AVLoadingIndicatorView);
        loadingText.setText(msg);
        mLoadingDialog = new Dialog(context, R.style.loading_dialog_style);
        mLoadingDialog.setCancelable(false);
        mLoadingDialog.setContentView(view, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT));

        mLoadingDialog.show();
        avLoadingIndicatorView.smoothToShow();
        mLoadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    mLoadingDialog.hide();
                    return true;
                }
                return false;
            }
        });

    }
    public static void stop(){
        mLoadingDialog.dismiss();
    }
}
