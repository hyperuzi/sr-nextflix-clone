import React from 'react'
import { Link } from 'react-router-dom'

const NotFoundPage = () => {
  return (
    <div className=' min-h-screen bg-cover bg-center flex flex-col justify-center items-center text-white notfound-bg' >
        <header className='absolute top-0 left-0 bg-black p-4 w-full'>
            <Link to = "/">
                <img src='/netflix-logo.png' alt='netflix-logo' className='h-8'/>
            </Link>
        </header>
        <div className='text-center error-page--content z-10' >
            <div className='text-7xl font-semibold mb-4'>Lost your way?</div>
            <div className='mb-6 text-3xl font-thin'>Sorry, we can't find that page. You'll find lots to explore on the home page.</div>
            <Link to = "/" className='bg-white max-w-sm text-black py-2 px-4 rounded-lg border-zinc-200 border-2 font-extralight'>
                Netflix Home 
            </Link>
            <div className='mt-6 border-l-2 border-l-red-600 pl-10 py-2 text-2xl text-white font-thin'>Error Code <span className='font-bold'>NSES-404</span></div>
            
        </div>
      
    </div>
  )
}

export default NotFoundPage
