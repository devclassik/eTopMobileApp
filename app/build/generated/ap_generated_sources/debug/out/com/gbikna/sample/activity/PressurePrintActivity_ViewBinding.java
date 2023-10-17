// Generated code from Butter Knife. Do not modify!
package com.gbikna.sample.activity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.gbikna.sample.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class PressurePrintActivity_ViewBinding implements Unbinder {
  private PressurePrintActivity target;

  private View view7f0a00c5;

  @UiThread
  public PressurePrintActivity_ViewBinding(PressurePrintActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public PressurePrintActivity_ViewBinding(final PressurePrintActivity target, View source) {
    this.target = target;

    View view;
    target.textView = Utils.findRequiredViewAsType(source, R.id.textView, "field 'textView'", TextView.class);
    view = Utils.findRequiredView(source, R.id.button, "field 'button' and method 'onClick'");
    target.button = Utils.castView(view, R.id.button, "field 'button'", Button.class);
    view7f0a00c5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
    target.tvCurrentTime = Utils.findRequiredViewAsType(source, R.id.currentTimes, "field 'tvCurrentTime'", TextView.class);
    target.tvSuccessTime = Utils.findRequiredViewAsType(source, R.id.successTime, "field 'tvSuccessTime'", TextView.class);
    target.tvFailTime = Utils.findRequiredViewAsType(source, R.id.failTime, "field 'tvFailTime'", TextView.class);
    target.etTestTime = Utils.findRequiredViewAsType(source, R.id.TestTimes, "field 'etTestTime'", EditText.class);
    target.tvFailList = Utils.findRequiredViewAsType(source, R.id.failList, "field 'tvFailList'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    PressurePrintActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textView = null;
    target.button = null;
    target.tvCurrentTime = null;
    target.tvSuccessTime = null;
    target.tvFailTime = null;
    target.etTestTime = null;
    target.tvFailList = null;

    view7f0a00c5.setOnClickListener(null);
    view7f0a00c5 = null;
  }
}
