// Generated code from Butter Knife. Do not modify!
package com.gbikna.sample.activity;

import android.view.View;
import android.widget.RadioButton;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.gbikna.sample.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class BeepActivity_ViewBinding implements Unbinder {
  private BeepActivity target;

  private View view7f0a00c5;

  @UiThread
  public BeepActivity_ViewBinding(BeepActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public BeepActivity_ViewBinding(final BeepActivity target, View source) {
    this.target = target;

    View view;
    target.normal = Utils.findRequiredViewAsType(source, R.id.normal, "field 'normal'", RadioButton.class);
    target.success = Utils.findRequiredViewAsType(source, R.id.success, "field 'success'", RadioButton.class);
    target.fail = Utils.findRequiredViewAsType(source, R.id.fail, "field 'fail'", RadioButton.class);
    target.interval = Utils.findRequiredViewAsType(source, R.id.interval, "field 'interval'", RadioButton.class);
    target.error = Utils.findRequiredViewAsType(source, R.id.error, "field 'error'", RadioButton.class);
    view = Utils.findRequiredView(source, R.id.button, "method 'onClick'");
    view7f0a00c5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    BeepActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.normal = null;
    target.success = null;
    target.fail = null;
    target.interval = null;
    target.error = null;

    view7f0a00c5.setOnClickListener(null);
    view7f0a00c5 = null;
  }
}
