
                String uid = mAuth.getCurrentUser().getUid();
                if(user == null){
                    Id.setText("You must login first!");
                    return;
                }