// Generated code from Butter Knife. Do not modify!
package com.gbikna.sample.activity;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.gbikna.sample.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class LedActivity_ViewBinding implements Unbinder {
  private LedActivity target;

  private View view7f0a00c5;

  @UiThread
  public LedActivity_ViewBinding(LedActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public LedActivity_ViewBinding(final LedActivity target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.button, "field 'button' and method 'onClick'");
    target.button = Utils.castView(view, R.id.button, "field 'button'", Button.class);
    view7f0a00c5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
    target.led1 = Utils.findRequiredViewAsType(source, R.id.led1, "field 'led1'", CheckBox.class);
    target.led2 = Utils.findRequiredViewAsType(source, R.id.led2, "field 'led2'", CheckBox.class);
    target.led3 = Utils.findRequiredViewAsType(source, R.id.led3, "field 'led3'", CheckBox.class);
    target.led4 = Utils.findRequiredViewAsType(source, R.id.led4, "field 'led4'", CheckBox.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    LedActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.button = null;
    target.led1 = null;
    target.led2 = null;
    target.led3 = null;
    target.led4 = null;

    view7f0a00c5.setOnClickListener(null);
    view7f0a00c5 = null;
  }
}
