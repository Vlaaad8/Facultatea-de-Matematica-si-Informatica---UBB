namespace Practic
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
            this.grupaView = new System.Windows.Forms.DataGridView();
            this.copilView = new System.Windows.Forms.DataGridView();
            this.numeBox = new System.Windows.Forms.TextBox();
            this.varstaBox = new System.Windows.Forms.TextBox();
            this.grupaBox = new System.Windows.Forms.TextBox();
            this.addButton = new System.Windows.Forms.Button();
            this.deleteButton = new System.Windows.Forms.Button();
            this.updateButton = new System.Windows.Forms.Button();
            this.Nume = new System.Windows.Forms.Label();
            this.Varsta = new System.Windows.Forms.Label();
            this.label1 = new System.Windows.Forms.Label();
            ((System.ComponentModel.ISupportInitialize)(this.grupaView)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.copilView)).BeginInit();
            this.SuspendLayout();
            // 
            // grupaView
            // 
            this.grupaView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.grupaView.Location = new System.Drawing.Point(56, 60);
            this.grupaView.Name = "grupaView";
            this.grupaView.RowHeadersWidth = 51;
            this.grupaView.RowTemplate.Height = 24;
            this.grupaView.Size = new System.Drawing.Size(468, 250);
            this.grupaView.TabIndex = 0;
            // 
            // copilView
            // 
            this.copilView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.copilView.Location = new System.Drawing.Point(715, 60);
            this.copilView.Name = "copilView";
            this.copilView.RowHeadersWidth = 51;
            this.copilView.RowTemplate.Height = 24;
            this.copilView.Size = new System.Drawing.Size(440, 250);
            this.copilView.TabIndex = 1;
            // 
            // numeBox
            // 
            this.numeBox.Location = new System.Drawing.Point(715, 344);
            this.numeBox.Name = "numeBox";
            this.numeBox.Size = new System.Drawing.Size(129, 22);
            this.numeBox.TabIndex = 2;
            // 
            // varstaBox
            // 
            this.varstaBox.Location = new System.Drawing.Point(715, 391);
            this.varstaBox.Name = "varstaBox";
            this.varstaBox.Size = new System.Drawing.Size(129, 22);
            this.varstaBox.TabIndex = 3;
            // 
            // grupaBox
            // 
            this.grupaBox.Location = new System.Drawing.Point(715, 428);
            this.grupaBox.Name = "grupaBox";
            this.grupaBox.Size = new System.Drawing.Size(129, 22);
            this.grupaBox.TabIndex = 4;
            // 
            // addButton
            // 
            this.addButton.Location = new System.Drawing.Point(999, 355);
            this.addButton.Name = "addButton";
            this.addButton.Size = new System.Drawing.Size(75, 23);
            this.addButton.TabIndex = 5;
            this.addButton.Text = "Add";
            this.addButton.UseVisualStyleBackColor = true;
            this.addButton.Click += new System.EventHandler(this.addButton_Click);
            // 
            // deleteButton
            // 
            this.deleteButton.Location = new System.Drawing.Point(999, 384);
            this.deleteButton.Name = "deleteButton";
            this.deleteButton.Size = new System.Drawing.Size(75, 23);
            this.deleteButton.TabIndex = 6;
            this.deleteButton.Text = "Delete";
            this.deleteButton.UseVisualStyleBackColor = true;
            this.deleteButton.Click += new System.EventHandler(this.deleteButton_Click);
            // 
            // updateButton
            // 
            this.updateButton.Location = new System.Drawing.Point(999, 413);
            this.updateButton.Name = "updateButton";
            this.updateButton.Size = new System.Drawing.Size(75, 23);
            this.updateButton.TabIndex = 7;
            this.updateButton.Text = "Update";
            this.updateButton.UseVisualStyleBackColor = true;
            this.updateButton.Click += new System.EventHandler(this.updateButton_Click);
            // 
            // Nume
            // 
            this.Nume.AutoSize = true;
            this.Nume.Location = new System.Drawing.Point(712, 325);
            this.Nume.Name = "Nume";
            this.Nume.Size = new System.Drawing.Size(43, 16);
            this.Nume.TabIndex = 8;
            this.Nume.Text = "Nume";
            this.Nume.Click += new System.EventHandler(this.label1_Click);
            // 
            // Varsta
            // 
            this.Varsta.AutoSize = true;
            this.Varsta.Location = new System.Drawing.Point(712, 372);
            this.Varsta.Name = "Varsta";
            this.Varsta.Size = new System.Drawing.Size(46, 16);
            this.Varsta.TabIndex = 9;
            this.Varsta.Text = "Varsta";
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(712, 416);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(57, 16);
            this.label1.TabIndex = 10;
            this.label1.Text = "GroupID";
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1325, 510);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.Varsta);
            this.Controls.Add(this.Nume);
            this.Controls.Add(this.updateButton);
            this.Controls.Add(this.deleteButton);
            this.Controls.Add(this.addButton);
            this.Controls.Add(this.grupaBox);
            this.Controls.Add(this.varstaBox);
            this.Controls.Add(this.numeBox);
            this.Controls.Add(this.copilView);
            this.Controls.Add(this.grupaView);
            this.Name = "Form1";
            this.Text = "Form1";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.bindingChild.CurrentChanged += new System.EventHandler(this.bindingChild_CurrentChanged);
            ((System.ComponentModel.ISupportInitialize)(this.grupaView)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.copilView)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.DataGridView grupaView;
        private System.Windows.Forms.DataGridView copilView;
        private System.Windows.Forms.TextBox numeBox;
        private System.Windows.Forms.TextBox varstaBox;
        private System.Windows.Forms.TextBox grupaBox;
        private System.Windows.Forms.Button addButton;
        private System.Windows.Forms.Button deleteButton;
        private System.Windows.Forms.Button updateButton;
        private System.Windows.Forms.Label Nume;
        private System.Windows.Forms.Label Varsta;
        private System.Windows.Forms.Label label1;
    }
}

