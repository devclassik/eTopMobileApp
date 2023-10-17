// Generated code from Butter Knife. Do not modify!
package com.gbikna.sample.dialog;

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

public class InputDialog_ViewBinding implements Unbinder {
  private InputDialog target;

  private View view7f0a008d;

  private View view7f0a008c;

  @UiThread
  public InputDialog_ViewBinding(InputDialog target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public InputDialog_ViewBinding(final InputDialog target, View source) {
    this.target = target;

    View view;
    view = Utils.findRequiredView(source, R.id.btnPositive, "field 'btnPositive' and method 'onClick'");
    target.btnPositive = Utils.castView(view, R.id.btnPositive, "field 'btnPositive'", Button.class);
    view7f0a008d = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.btnNegtive, "field 'btnNegtive' and method 'onClick'");
    target.btnNegtive = Utils.castView(view, R.id.btnNegtive, "field 'btnNegtive'", Button.class);
    view7f0a008c = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    target.tvTitle = Utils.findRequiredViewAsType(source, R.id.tvTitle, "field 'tvTitle'", TextView.class);
    target.etInput = Utils.findRequiredViewAsType(source, R.id.etInput, "field 'etInput'", EditText.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    InputDialog target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.btnPositive = null;
    target.btnNegtive = null;
    target.tvTitle = null;
    target.etInput = null;

    view7f0a008d.setOnClickListener(null);
    view7f0a008d = null;
    view7f0a008c.setOnClickListener(null);
    view7f0a008c = null;
  }
}
