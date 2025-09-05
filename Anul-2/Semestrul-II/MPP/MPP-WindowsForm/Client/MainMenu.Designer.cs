using System.ComponentModel;

namespace MPP_WindowsForm;

partial class MainMenu
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
        mainTable = new System.Windows.Forms.DataGridView();
        originBox = new System.Windows.Forms.ComboBox();
        departureBox = new System.Windows.Forms.ComboBox();
        dateBox = new System.Windows.Forms.DateTimePicker();
        searchButton1 = new System.Windows.Forms.Button();
        logOutButton = new System.Windows.Forms.Button();
        label1 = new System.Windows.Forms.Label();
        label2 = new System.Windows.Forms.Label();
        ((System.ComponentModel.ISupportInitialize)mainTable).BeginInit();
        SuspendLayout();
        // 
        // mainTable
        // 
        mainTable.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
        mainTable.Location = new System.Drawing.Point(55, 96);
        mainTable.Name = "mainTable";
        mainTable.RowHeadersWidth = 51;
        mainTable.Size = new System.Drawing.Size(691, 316);
        mainTable.TabIndex = 0;
        mainTable.Text = "dataGridView1";
        // 
        // originBox
        // 
        originBox.FormattingEnabled = true;
        originBox.Location = new System.Drawing.Point(75, 39);
        originBox.Name = "originBox";
        originBox.Size = new System.Drawing.Size(121, 28);
        originBox.TabIndex = 1;
        // 
        // departureBox
        // 
        departureBox.FormattingEnabled = true;
        departureBox.Location = new System.Drawing.Point(254, 39);
        departureBox.Name = "departureBox";
        departureBox.Size = new System.Drawing.Size(121, 28);
        departureBox.TabIndex = 2;
        // 
        // dateBox
        // 
        dateBox.Location = new System.Drawing.Point(416, 39);
        dateBox.Name = "dateBox";
        dateBox.Size = new System.Drawing.Size(200, 27);
        dateBox.TabIndex = 3;
        // 
        // searchButton1
        // 
        searchButton1.Location = new System.Drawing.Point(644, 39);
        searchButton1.Name = "searchButton1";
        searchButton1.Size = new System.Drawing.Size(102, 28);
        searchButton1.TabIndex = 4;
        searchButton1.Text = "Search";
        searchButton1.UseVisualStyleBackColor = true;
        searchButton1.Click += searchButton1_Click;
        // 
        // logOutButton
        // 
        logOutButton.Location = new System.Drawing.Point(612, 433);
        logOutButton.Name = "logOutButton";
        logOutButton.Size = new System.Drawing.Size(134, 42);
        logOutButton.TabIndex = 5;
        logOutButton.Text = "Log out";
        logOutButton.UseVisualStyleBackColor = true;
        logOutButton.Click += logOutButton_Click;
        // 
        // label1
        // 
        label1.Location = new System.Drawing.Point(75, 13);
        label1.Name = "label1";
        label1.Size = new System.Drawing.Size(100, 23);
        label1.TabIndex = 6;
        label1.Text = "From:";
        // 
        // label2
        // 
        label2.Location = new System.Drawing.Point(254, 13);
        label2.Name = "label2";
        label2.Size = new System.Drawing.Size(100, 23);
        label2.TabIndex = 7;
        label2.Text = "To:";
        // 
        // MainMenu
        // 
        AutoScaleDimensions = new System.Drawing.SizeF(8F, 20F);
        AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
        ClientSize = new System.Drawing.Size(800, 491);
        Controls.Add(label2);
        Controls.Add(label1);
        Controls.Add(logOutButton);
        Controls.Add(searchButton1);
        Controls.Add(dateBox);
        Controls.Add(departureBox);
        Controls.Add(originBox);
        Controls.Add(mainTable);
        Text = "MainMenu";
        ((System.ComponentModel.ISupportInitialize)mainTable).EndInit();
        ResumeLayout(false);
    }

    private System.Windows.Forms.Label label1;
    private System.Windows.Forms.Label label2;

    private System.Windows.Forms.Button logOutButton;

    private System.Windows.Forms.DataGridView mainTable;
    private System.Windows.Forms.ComboBox originBox;
    private System.Windows.Forms.ComboBox departureBox;
    private System.Windows.Forms.DateTimePicker dateBox;
    private System.Windows.Forms.Button searchButton1;

    #endregion
}