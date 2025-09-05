namespace Exercitiul_1;

partial class Form1
{
    /// <summary>
    ///  Required designer variable.
    /// </summary>
    private System.ComponentModel.IContainer components = null;

    /// <summary>
    ///  Clean up any resources being used.
    /// </summary>
    /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
    protected override void Dispose(bool disposing)
    {
        if (disposing && (components != null))
        {
            components.Dispose();
        }

        base.Dispose(disposing);
    }

    #region Windows Form Designer generated code

    /// <summary>
    /// Required method for Designer support - do not modify
    /// the contents of this method with the code editor.
    /// </summary>
    private void InitializeComponent()
    {
        dataGridView1 = new System.Windows.Forms.DataGridView();
        dataGridView2 = new System.Windows.Forms.DataGridView();
        textBox1 = new System.Windows.Forms.TextBox();
        textBox2 = new System.Windows.Forms.TextBox();
        ((System.ComponentModel.ISupportInitialize)dataGridView1).BeginInit();
        ((System.ComponentModel.ISupportInitialize)dataGridView2).BeginInit();
        SuspendLayout();
        // 
        // dataGridView1
        // 
        dataGridView1.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        dataGridView1.Location = new System.Drawing.Point(55, 25);
        dataGridView1.Name = "dataGridView1";
        dataGridView1.RowHeadersWidth = 51;
        dataGridView1.Size = new System.Drawing.Size(418, 261);
        dataGridView1.TabIndex = 0;
        dataGridView1.Text = "dataGridView1";
        // 
        // dataGridView2
        // 
        dataGridView2.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        dataGridView2.Location = new System.Drawing.Point(552, 25);
        dataGridView2.Name = "dataGridView2";
        dataGridView2.RowHeadersWidth = 51;
        dataGridView2.Size = new System.Drawing.Size(439, 261);
        dataGridView2.TabIndex = 1;
        dataGridView2.Text = "dataGridView2";
        // 
        // textBox1
        // 
        textBox1.Location = new System.Drawing.Point(412, 321);
        textBox1.Name = "textBox1";
        textBox1.Size = new System.Drawing.Size(100, 27);
        textBox1.TabIndex = 2;
        // 
        // textBox2
        // 
        textBox2.Location = new System.Drawing.Point(412, 364);
        textBox2.Name = "textBox2";
        textBox2.Size = new System.Drawing.Size(100, 27);
        textBox2.TabIndex = 3;
        // 
        // Form1
        // 
        AutoScaleDimensions = new System.Drawing.SizeF(8F, 20F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        ClientSize = new System.Drawing.Size(1056, 525);
        Controls.Add(textBox2);
        Controls.Add(textBox1);
        Controls.Add(dataGridView2);
        Controls.Add(dataGridView1);
        Text = "Form1";
        ((System.ComponentModel.ISupportInitialize)dataGridView1).EndInit();
        ((System.ComponentModel.ISupportInitialize)dataGridView2).EndInit();
        ResumeLayout(false);
        PerformLayout();
    }

    private System.Windows.Forms.TextBox textBox1;
    private System.Windows.Forms.TextBox textBox2;

    private System.Windows.Forms.DataGridView dataGridView1;
    private System.Windows.Forms.DataGridView dataGridView2;

    #endregion
}