const {app, express}=require('./router');





const PORT = process.env.PORT || 3001;

app.listen(PORT, 'localhost', ()=>{
    console.log(`Server is local internet on port ${PORT}.`);
})