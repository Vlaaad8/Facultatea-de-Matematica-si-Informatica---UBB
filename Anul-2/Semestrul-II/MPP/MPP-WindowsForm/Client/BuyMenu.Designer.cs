using System.ComponentModel;

namespace MPP_WindowsForm;

partial class BuyMenu
{
    /// <summary>
    /// Required designer variable.
    /// </summary>
    private IContainer components = null;

    /// <summary>
    /// Clean up any resources being used.
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
        buyersBox = new System.Windows.Forms.TextBox();
        numberBox = new System.Windows.Forms.NumericUpDown();
        buyButton = new System.Windows.Forms.Button();
        goBackButton = new System.Windows.Forms.Button();
        label1 = new System.Windows.Forms.Label();
        label2 = new System.Windows.Forms.Label();
        ((System.ComponentModel.ISupportInitialize)dataGridView1).BeginInit();
        ((System.ComponentModel.ISupportInitialize)numberBox).BeginInit();
        SuspendLayout();
        // 
        // dataGridView1
        // 
        dataGridView1.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        dataGridView1.Location = new System.Drawing.Point(51, 135);
        dataGridView1.Name = "dataGridView1";
        dataGridView1.RowHeadersWidth = 51;
        dataGridView1.Size = new System.Drawing.Size(687, 291);
        dataGridView1.TabIndex = 0;
        dataGridView1.Text = "dataGridView1";
        // 
        // buyersBox
        // 
        buyersBox.Location = new System.Drawing.Point(322, 54);
        buyersBox.Name = "buyersBox";
        buyersBox.Size = new System.Drawing.Size(198, 27);
        buyersBox.TabIndex = 1;
        // 
        // numberBox
        // 
        numberBox.Location = new System.Drawing.Point(87, 54);
        numberBox.Name = "numberBox";
        numberBox.Size = new System.Drawing.Size(193, 27);
        numberBox.TabIndex = 2;
        // 
        // buyButton
        // 
        buyButton.Location = new System.Drawing.Point(582, 54);
        buyButton.Name = "buyButton";
        buyButton.Size = new System.Drawing.Size(113, 27);
        buyButton.TabIndex = 3;
        buyButton.Text = "Buy";
        buyButton.UseVisualStyleBackColor = true;
        buyButton.Click += buyButton_Click;
        // 
        // goBackButton
        // 
        goBackButton.Location = new System.Drawing.Point(663, 432);
        goBackButton.Name = "goBackButton";
        goBackButton.Size = new System.Drawing.Size(117, 36);
        goBackButton.TabIndex = 4;
        goBackButton.Text = "Go Back";
        goBackButton.UseVisualStyleBackColor = true;
        goBackButton.Click += goBackButton_Click;
        // 
        // label1
        // 
        label1.Location = new System.Drawing.Point(87, 28);
        label1.Name = "label1";
        label1.Size = new System.Drawing.Size(147, 23);
        label1.TabIndex = 5;
        label1.Text = "Number of tickets:";
        // 
        // label2
        // 
        label2.Location = new System.Drawing.Point(322, 28);
        label2.Name = "label2";
        label2.Size = new System.Drawing.Size(132, 23);
        label2.TabIndex = 6;
        label2.Text = "Name of people:";
        // 
        // BuyMenu
        // 
        AutoScaleDimensions = new System.Drawing.SizeF(8F, 20F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        ClientSize = new System.Drawing.Size(800, 473);
        Controls.Add(label2);
        Controls.Add(label1);
        Controls.Add(goBackButton);
        Controls.Add(buyButton);
        Controls.Add(numberBox);
        Controls.Add(buyersBox);
        Controls.Add(dataGridView1);
        Text = "BuyMenu";
        ((System.ComponentModel.ISupportInitialize)dataGridView1).EndInit();
        ((System.ComponentModel.ISupportInitialize)numberBox).EndInit();
        ResumeLayout(false);
        PerformLayout();
    }

    private System.Windows.Forms.Label label1;
    private System.Windows.Forms.Label label2;

    private System.Windows.Forms.Button goBackButton;

    private System.Windows.Forms.NumericUpDown numberBox;
    private System.Windows.Forms.Button buyButton;

    private System.Windows.Forms.DataGridView dataGridView1;
    private System.Windows.Forms.TextBox buyersBox;

    #endregion
}