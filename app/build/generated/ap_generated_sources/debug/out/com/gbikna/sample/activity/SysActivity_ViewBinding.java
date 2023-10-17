// Generated code from Butter Knife. Do not modify!
package com.gbikna.sample.activity;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.annotation.UiThread;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import com.gbikna.sample.R;
import java.lang.IllegalStateException;
import java.lang.Override;

public class SysActivity_ViewBinding implements Unbinder {
  private SysActivity target;

  private View view7f0a02bf;

  private View view7f0a0203;

  private View view7f0a016b;

  private View view7f0a0064;

  private View view7f0a0065;

  private View view7f0a017e;

  private View view7f0a017f;

  private View view7f0a0342;

  private View view7f0a0276;

  private View view7f0a01c8;

  private View view7f0a01e3;

  private View view7f0a0278;

  private View view7f0a01b4;

  private View view7f0a01b5;

  private View view7f0a01b0;

  private View view7f0a033e;

  private View view7f0a01c9;

  private View view7f0a02ba;

  @UiThread
  public SysActivity_ViewBinding(SysActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public SysActivity_ViewBinding(final SysActivity target, View source) {
    this.target = target;

    View view;
    target.textView = Utils.findRequiredViewAsType(source, R.id.textView, "field 'textView'", TextView.class);
    view = Utils.findRequiredView(source, R.id.sn, "method 'onClick'");
    view7f0a02bf = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.model, "method 'onClick'");
    view7f0a0203 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.firmware, "method 'onClick'");
    view7f0a016b = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.app0, "method 'onClick'");
    view7f0a0064 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.app1, "method 'onClick'");
    view7f0a0065 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.gpsoff, "method 'onClick'");
    view7f0a017e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.gpson, "method 'onClick'");
    view7f0a017f = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.updatefm, "method 'onClick'");
    view7f0a0342 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.reboot, "method 'onClick'");
    view7f0a0276 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.launcher1, "method 'onClick'");
    view7f0a01c8 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.log, "method 'onClick'");
    view7f0a01e3 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.recovery, "method 'onClick'");
    view7f0a0278 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.isfore, "method 'onClick'");
    view7f0a01b4 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.isnotfore, "method 'onClick'");
    view7f0a01b5 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.install, "method 'onClick'");
    view7f0a01b0 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.uninstall, "method 'onClick'");
    view7f0a033e = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.launcher2, "method 'onClick'");
    view7f0a01c9 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.sim, "method 'onClick'");
    view7f0a02ba = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClick(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    SysActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.textView = null;

    view7f0a02bf.setOnClickListener(null);
    view7f0a02bf = null;
    view7f0a0203.setOnClickListener(null);
    view7f0a0203 = null;
    view7f0a016b.setOnClickListener(null);
    view7f0a016b = null;
    view7f0a0064.setOnClickListener(null);
    view7f0a0064 = null;
    view7f0a0065.setOnClickListener(null);
    view7f0a0065 = null;
    view7f0a017e.setOnClickListener(null);
    view7f0a017e = null;
    view7f0a017f.setOnClickListener(null);
    view7f0a017f = null;
    view7f0a0342.setOnClickListener(null);
    view7f0a0342 = null;
    view7f0a0276.setOnClickListener(null);
    view7f0a0276 = null;
    view7f0a01c8.setOnClickListener(null);
    view7f0a01c8 = null;
    view7f0a01e3.setOnClickListener(null);
    view7f0a01e3 = null;
    view7f0a0278.setOnClickListener(null);
    view7f0a0278 = null;
    view7f0a01b4.setOnClickListener(null);
    view7f0a01b4 = null;
    view7f0a01b5.setOnClickListener(null);
    view7f0a01b5 = null;
    view7f0a01b0.setOnClickListener(null);
    view7f0a01b0 = null;
    view7f0a033e.setOnClickListener(null);
    view7f0a033e = null;
    view7f0a01c9.setOnClickListener(null);
    view7f0a01c9 = null;
    view7f0a02ba.setOnClickListener(null);
    view7f0a02ba = null;
  }
}
