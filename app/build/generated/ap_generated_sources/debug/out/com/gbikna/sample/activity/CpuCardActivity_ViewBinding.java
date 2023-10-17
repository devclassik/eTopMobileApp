// Generated code from Butter Knife. Do not modify!
package com.gbikna.sample.activity;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.gbikna.sample.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class CpuCardActivity_ViewBinding implements Unbinder {
  private CpuCardActivity target;

  private View view7f0a00c5;

  @UiThread
  public CpuCardActivity_ViewBinding(CpuCardActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public CpuCardActivity_ViewBinding(final CpuCardActivity target, View source) {
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
  }

  @Override
  @CallSuper
  public void unbind() {
    CpuCardActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textView = null;
    target.button = null;

    view7f0a00c5.setOnClickListener(null);
    view7f0a00c5 = null;
  }
}
