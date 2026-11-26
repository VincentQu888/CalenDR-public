const listObserver = new IntersectionObserver((entries) => {
  entries.forEach((entry) =>{
    if(entry.isIntersecting){
      const animationItems = document.querySelectorAll('.list-animation-item');
      for(i = 0; i < animationItems.length; ++i) animationItems[i].classList.add('list-animation-item-shown');
    }
  });
});

const listAnimationHiddenElements = document.querySelectorAll('.image-section');
listAnimationHiddenElements.forEach((el) => listObserver.observe(el));


document.addEventListener('DOMContentLoaded', function(){
  alert("Welcome to CalenDR! We are currently in open beta testing.\n\nThis version is still in development, and you may encounter bugs or incomplete features. Your usage and feedback will help us identify issues and improve the site before the official launch.")
})