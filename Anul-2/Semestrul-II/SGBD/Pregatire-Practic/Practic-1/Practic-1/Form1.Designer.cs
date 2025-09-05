namespace Practic_1
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
            this.taraView = new System.Windows.Forms.DataGridView();
            this.clientView = new System.Windows.Forms.DataGridView();
            this.cod_client = new System.Windows.Forms.TextBox();
            this.nume = new System.Windows.Forms.TextBox();
            this.email = new System.Windows.Forms.TextBox();
            this.cod_tara = new System.Windows.Forms.TextBox();
            this.button1 = new System.Windows.Forms.Button();
            this.deleteButton = new System.Windows.Forms.Button();
            this.updateButton = new System.Windows.Forms.Button();
            this.data_nasterii = new System.Windows.Forms.DateTimePicker();
            ((System.ComponentModel.ISupportInitialize)(this.taraView)).BeginInit();
            ((System.ComponentModel.ISupportInitialize)(this.clientView)).BeginInit();
            this.SuspendLayout();
            // 
            // taraView
            // 
            this.taraView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.taraView.Location = new System.Drawing.Point(76, 45);
            this.taraView.Name = "taraView";
            this.taraView.RowHeadersWidth = 51;
            this.taraView.RowTemplate.Height = 24;
            this.taraView.Size = new System.Drawing.Size(416, 236);
            this.taraView.TabIndex = 0;
            // 
            // clientView
            // 
            this.clientView.ColumnHeadersHeightSizeMode = System.Windows.Forms.DataGridViewColumnHeadersHeightSizeMode.AutoSize;
            this.clientView.Location = new System.Drawing.Point(701, 52);
            this.clientView.Name = "clientView";
            this.clientView.RowHeadersWidth = 51;
            this.clientView.RowTemplate.Height = 24;
            this.clientView.Size = new System.Drawing.Size(430, 229);
            this.clientView.TabIndex = 1;
            // 
            // cod_client
            // 
            this.cod_client.Location = new System.Drawing.Point(438, 332);
            this.cod_client.Name = "cod_client";
            this.cod_client.Size = new System.Drawing.Size(166, 22);
            this.cod_client.TabIndex = 2;
            // 
            // nume
            // 
            this.nume.Location = new System.Drawing.Point(438, 370);
            this.nume.Name = "nume";
            this.nume.Size = new System.Drawing.Size(165, 22);
            this.nume.TabIndex = 3;
            // 
            // email
            // 
            this.email.Location = new System.Drawing.Point(438, 407);
            this.email.Name = "email";
            this.email.Size = new System.Drawing.Size(161, 22);
            this.email.TabIndex = 4;
            // 
            // cod_tara
            // 
            this.cod_tara.Location = new System.Drawing.Point(438, 488);
            this.cod_tara.Name = "cod_tara";
            this.cod_tara.Size = new System.Drawing.Size(160, 22);
            this.cod_tara.TabIndex = 6;
            // 
            // button1
            // 
            this.button1.Location = new System.Drawing.Point(768, 380);
            this.button1.Name = "button1";
            this.button1.Size = new System.Drawing.Size(75, 23);
            this.button1.TabIndex = 7;
            this.button1.Text = "Add";
            this.button1.UseVisualStyleBackColor = true;
            this.button1.Click += new System.EventHandler(this.button1_Click);
            // 
            // deleteButton
            // 
            this.deleteButton.Location = new System.Drawing.Point(768, 418);
            this.deleteButton.Name = "deleteButton";
            this.deleteButton.Size = new System.Drawing.Size(75, 23);
            this.deleteButton.TabIndex = 8;
            this.deleteButton.Text = "Delete";
            this.deleteButton.UseVisualStyleBackColor = true;
            this.deleteButton.Click += new System.EventHandler(this.deleteButton_Click);
            // 
            // updateButton
            // 
            this.updateButton.Location = new System.Drawing.Point(768, 447);
            this.updateButton.Name = "updateButton";
            this.updateButton.Size = new System.Drawing.Size(75, 23);
            this.updateButton.TabIndex = 9;
            this.updateButton.Text = "Update";
            this.updateButton.UseVisualStyleBackColor = true;
            this.updateButton.Click += new System.EventHandler(this.updateButton_Click);
            // 
            // data_nasterii
            // 
            this.data_nasterii.Location = new System.Drawing.Point(438, 445);
            this.data_nasterii.Name = "data_nasterii";
            this.data_nasterii.Size = new System.Drawing.Size(200, 22);
            this.data_nasterii.TabIndex = 10;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(8F, 16F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(1203, 522);
            this.Controls.Add(this.data_nasterii);
            this.Controls.Add(this.updateButton);
            this.Controls.Add(this.deleteButton);
            this.Controls.Add(this.button1);
            this.Controls.Add(this.cod_tara);
            this.Controls.Add(this.email);
            this.Controls.Add(this.nume);
            this.Controls.Add(this.cod_client);
            this.Controls.Add(this.clientView);
            this.Controls.Add(this.taraView);
            this.Name = "Form1";
            this.Text = "Form1";
            this.Load += new System.EventHandler(this.Form1_Load);
            this.bindingChild.CurrentChanged += new System.EventHandler(this.bindingChild_CurrentChanged);
            ((System.ComponentModel.ISupportInitialize)(this.taraView)).EndInit();
            ((System.ComponentModel.ISupportInitialize)(this.clientView)).EndInit();
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.DataGridView taraView;
        private System.Windows.Forms.DataGridView clientView;
        private System.Windows.Forms.TextBox cod_client;
        private System.Windows.Forms.TextBox nume;
        private System.Windows.Forms.TextBox email;
        private System.Windows.Forms.TextBox cod_tara;
        private System.Windows.Forms.Button button1;
        private System.Windows.Forms.Button deleteButton;
        private System.Windows.Forms.Button updateButton;
        private System.Windows.Forms.DateTimePicker data_nasterii;
    }
}

