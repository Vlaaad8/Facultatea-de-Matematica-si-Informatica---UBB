namespace Simulare_2
{
    partial class Form1
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

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
            this.omView = new System.Windows.Forms.DataGridView();
            this.caineView = new System.Windows.Forms.DataGridView();
            this.varstaBox = new System.Windows.Forms.TextBox();
            this.numeBox = new System.Windows.Forms.TextBox();
            this.rasaBox = new System.Windows.Forms.TextBox();
            this.omBox = new System.Windows.Forms.TextBox();
            this.addButton = new System.Windows.Forms.Button();
            this.deleteButton = new System.Windows.Forms.Button();
            this.updateButton = new System.Windows.Forms.Button();
            ((System.ComponentModel.ISupportInitialize)(this.omView)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.caineView)).BeginInit();
            this.SuspendLayout();
            // 
            // omView
            // 
            this.omView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.omView.Location = new System.Drawing.Point(43, 34);
            this.omView.Name = "omView";
            this.omView.RowHeadersWidth = 51;
            this.omView.RowTemplate.Height = 24;
            this.omView.Size = new System.Drawing.Size(414, 246);
            this.omView.TabIndex = 0;
            // 
            // caineView
            // 
            this.caineView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.caineView.Location = new System.Drawing.Point(636, 34);
            this.caineView.Name = "caineView";
            this.caineView.RowHeadersWidth = 51;
            this.caineView.RowTemplate.Height = 24;
            this.caineView.Size = new System.Drawing.Size(402, 246);
            this.caineView.TabIndex = 1;
            // 
            // varstaBox
            // 
            this.varstaBox.Location = new System.Drawing.Point(647, 310);
            this.varstaBox.Name = "varstaBox";
            this.varstaBox.Size = new System.Drawing.Size(149, 22);
            this.varstaBox.TabIndex = 2;
            // 
            // numeBox
            // 
            this.numeBox.Location = new System.Drawing.Point(647, 351);
            this.numeBox.Name = "numeBox";
            this.numeBox.Size = new System.Drawing.Size(149, 22);
            this.numeBox.TabIndex = 3;
            // 
            // rasaBox
            // 
            this.rasaBox.Location = new System.Drawing.Point(647, 389);
            this.rasaBox.Name = "rasaBox";
            this.rasaBox.Size = new System.Drawing.Size(149, 22);
            this.rasaBox.TabIndex = 4;
            // 
            // omBox
            // 
            this.omBox.Location = new System.Drawing.Point(647, 432);
            this.omBox.Name = "omBox";
            this.omBox.Size = new System.Drawing.Size(149, 22);
            this.omBox.TabIndex = 5;
            // 
            // addButton
            // 
            this.addButton.Location = new System.Drawing.Point(931, 334);
            this.addButton.Name = "addButton";
            this.addButton.Size = new System.Drawing.Size(75, 23);
            this.addButton.TabIndex = 6;
            this.addButton.Text = "Add";
            this.addButton.UseVisualStyleBackColor = true;
            this.addButton.Click += new System.EventHandler(this.addButton_Click);
            // 
            // deleteButton
            // 
            this.deleteButton.Location = new System.Drawing.Point(931, 363);
            this.deleteButton.Name = "deleteButton";
            this.deleteButton.Size = new System.Drawing.Size(75, 23);
            this.deleteButton.TabIndex = 7;
            this.deleteButton.Text = "Delete";
            this.deleteButton.UseVisualStyleBackColor = true;
            this.deleteButton.Click += new System.EventHandler(this.deleteButton_Click);
            // 
            // updateButton
            // 
            this.updateButton.Location = new System.Drawing.Point(931, 392);
            this.updateButton.Name = "updateButton";
            this.updateButton.Size = new System.Drawing.Size(75, 23);
            this.updateButton.TabIndex = 8;
            this.updateButton.Text = "Update";
            this.updateButton.UseVisualStyleBackColor = true;
            this.updateButton.Click += new System.EventHandler(this.updateButton_Click);
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1136, 481);
            this.Controls.Add(this.updateButton);
            this.Controls.Add(this.deleteButton);
            this.Controls.Add(this.addButton);
            this.Controls.Add(this.omBox);
            this.Controls.Add(this.rasaBox);
            this.Controls.Add(this.numeBox);
            this.Controls.Add(this.varstaBox);
            this.Controls.Add(this.caineView);
            this.Controls.Add(this.omView);
            this.Name = "Form1";
            this.Text = "Form1";
            this.bindingChild.CurrentChanged += new System.EventHandler(this.bindingChild_CurrentChanged);
            this.Load += new System.EventHandler(this.Form1_Load);
            ((System.ComponentModel.ISupportInitialize)(this.omView)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.caineView)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.DataGridView omView;
        private System.Windows.Forms.DataGridView caineView;
        private System.Windows.Forms.TextBox varstaBox;
        private System.Windows.Forms.TextBox numeBox;
        private System.Windows.Forms.TextBox rasaBox;
        private System.Windows.Forms.TextBox omBox;
        private System.Windows.Forms.Button addButton;
        private System.Windows.Forms.Button deleteButton;
        private System.Windows.Forms.Button updateButton;
    }
}

